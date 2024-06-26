package com.tokopedia.content.product.picker.seller.view.bottomsheet

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.content.common.ui.model.orUnknown
import com.tokopedia.content.common.util.eventbus.EventBus
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.content.product.picker.R
import com.tokopedia.content.product.picker.seller.analytic.manager.EtalaseListAnalyticManager
import com.tokopedia.content.product.picker.seller.model.uimodel.CampaignAndEtalaseUiModel
import com.tokopedia.content.product.picker.seller.model.uimodel.ProductSetupAction
import com.tokopedia.content.product.picker.seller.model.ProductListPaging
import com.tokopedia.content.product.picker.seller.view.viewcomponent.EtalaseListViewComponent
import com.tokopedia.content.common.util.bottomsheet.ContentDialogCustomizer
import com.tokopedia.content.product.picker.databinding.BottomSheetSellerEtalaseListBinding
import com.tokopedia.play_common.lifecycle.lifecycleBound
import com.tokopedia.play_common.util.extension.withCache
import com.tokopedia.play_common.viewcomponent.viewComponent
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

/**
 * Created by kenny.hadisaputra on 26/01/22
 */
class EtalaseListBottomSheet @Inject constructor(
    private val dialogCustomizer: ContentDialogCustomizer,
    private val analyticManager: EtalaseListAnalyticManager,
) : BaseProductSetupBottomSheet() {

    private var _binding: BottomSheetSellerEtalaseListBinding? = null
    private val binding: BottomSheetSellerEtalaseListBinding
        get() = _binding!!

    private var mDataSource: DataSource? = null

    private val eventBus by lifecycleBound(
        creator = { EventBus<Any>() }
    )

    private val etalaseListView by viewComponent {
        EtalaseListViewComponent(binding.rvEtalase, eventBus)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            dialogCustomizer.customize(this)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBottomSheet()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        setupObserve()
        setupAnalytic()
    }

    override fun onStart() {
        super.onStart()
        eventBus.emit(Event.ViewBottomSheet)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, TAG)
    }

    fun setDataSource(dataSource: DataSource?) {
        mDataSource = dataSource
    }

    private fun setupBottomSheet() {
        _binding = BottomSheetSellerEtalaseListBinding.inflate(
            LayoutInflater.from(requireContext()),
        )
        setChild(binding.root)

        setOnDismissListener {
            eventBus.emit(Event.ClickClose)
        }
    }

    private fun setupView() {
        binding.root.layoutParams = binding.root.layoutParams.apply {
            height = (getScreenHeight() * 0.8f).toInt()
        }
    }

    private fun setupObserve() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiState.withCache().collectLatest { (prevState, state) ->
                renderBottomSheetTitle(state.campaignAndEtalase)
                renderEtalaseList(
                    prevState?.campaignAndEtalase,
                    state.campaignAndEtalase,
                    state.loadParam,
                )
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            eventBus.subscribe().collect {
                when (it) {
                    is EtalaseListViewComponent.Event -> handleEtalaseListEvent(it)
                }
            }
        }

        analyticManager.observe(viewLifecycleOwner.lifecycleScope, eventBus)
    }

    private fun setupAnalytic() {
        eventBus.emit(Event.SetSelectedAccount(mDataSource?.getSelectedAccount().orUnknown()))
    }

    private fun renderBottomSheetTitle(
        model: CampaignAndEtalaseUiModel,
    ) {
        val title = if (model.campaignList.isNotEmpty() && model.etalaseList.isNotEmpty()) {
            getString(R.string.campaign_and_etalase)
        } else if (model.campaignList.isNotEmpty()) {
            getString(R.string.campaign)
        } else {
            getString(R.string.etalase)
        }
        setTitle(title)
    }

    private fun renderEtalaseList(
        prevModel: CampaignAndEtalaseUiModel?,
        model: CampaignAndEtalaseUiModel,
        loadParam: ProductListPaging.Param,
    ) {
        if (prevModel?.campaignList == model.campaignList &&
            prevModel.etalaseList == model.etalaseList) return

        etalaseListView.setCampaignAndEtalaseList(
            campaignList = model.campaignList,
            etalaseList = model.etalaseList,
            selected = loadParam.etalase,
        )
    }

    /**
     * View Event
     */
    private fun handleEtalaseListEvent(event: EtalaseListViewComponent.Event) {
        when (event) {
            is EtalaseListViewComponent.Event.OnCampaignSelected -> {
                viewModel.submitAction(ProductSetupAction.SelectCampaign(event.campaign))
                dismiss()
            }
            is EtalaseListViewComponent.Event.OnEtalaseSelected -> {
                viewModel.submitAction(ProductSetupAction.SelectEtalase(event.etalase))
                dismiss()
            }
        }
    }

    interface DataSource {
        fun getSelectedAccount(): ContentAccountUiModel
    }

    sealed interface Event {
        data class SetSelectedAccount(val account: ContentAccountUiModel) : Event
        object ClickClose : Event
        object ViewBottomSheet : Event
    }

    companion object {
        private const val TAG = "PlayBroEtalaseAndCampaignListBottomSheet"

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader
        ): EtalaseListBottomSheet {
            val oldInstance = fragmentManager.findFragmentByTag(TAG) as? EtalaseListBottomSheet
            return if (oldInstance != null) oldInstance
            else {
                val fragmentFactory = fragmentManager.fragmentFactory
                fragmentFactory.instantiate(
                    classLoader,
                    EtalaseListBottomSheet::class.java.name
                ) as EtalaseListBottomSheet
            }
        }
    }
}
