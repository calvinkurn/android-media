package com.tokopedia.play.broadcaster.view.fragment.setup.etalase

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.transition.Fade
import androidx.transition.Slide
import androidx.transition.TransitionSet
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.analytic.PlayBroadcastAnalytic
import com.tokopedia.play.broadcaster.util.extension.showToaster
import com.tokopedia.play.broadcaster.util.pageflow.FragmentPageNavigator
import com.tokopedia.play.broadcaster.view.contract.PlayEtalaseSetupCoordinator
import com.tokopedia.play.broadcaster.view.contract.ProductSetupListener
import com.tokopedia.play.broadcaster.view.custom.PlayBottomSheetHeader
import com.tokopedia.play.broadcaster.view.custom.PlaySearchBar
import com.tokopedia.play.broadcaster.view.fragment.base.PlayBaseEtalaseSetupFragment
import com.tokopedia.play.broadcaster.view.fragment.base.PlayBaseSetupFragment
import com.tokopedia.play.broadcaster.view.partial.BottomActionViewComponent
import com.tokopedia.play.broadcaster.view.partial.SelectedProductPageViewComponent
import com.tokopedia.play.broadcaster.view.viewmodel.DataStoreViewModel
import com.tokopedia.play.broadcaster.view.viewmodel.PlayEtalasePickerViewModel
import com.tokopedia.play_common.lifecycle.lifecycleBound
import com.tokopedia.play_common.model.result.NetworkResult
import com.tokopedia.play_common.viewcomponent.viewComponent
import com.tokopedia.unifycomponents.Toaster
import kotlinx.coroutines.*
import javax.inject.Inject

/**
 * Created by jegul on 26/05/20
 */
class PlayEtalasePickerFragment @Inject constructor(
        private val viewModelFactory: ViewModelFactory,
        dispatcher: CoroutineDispatchers,
        private val analytic: PlayBroadcastAnalytic
) : PlayBaseSetupFragment(), PlayEtalaseSetupCoordinator {

    private val job = SupervisorJob()
    private val scope = CoroutineScope(dispatcher.main + job)

    private lateinit var viewModel: PlayEtalasePickerViewModel
    private lateinit var dataStoreViewModel: DataStoreViewModel

    private lateinit var container: ViewGroup
    private lateinit var tvInfo: TextView
    private lateinit var flEtalaseFlow: FrameLayout
    private lateinit var psbSearch: PlaySearchBar
    private lateinit var errorEtalase: GlobalError
    private lateinit var bottomSheetHeader : PlayBottomSheetHeader

    private val pageNavigator: FragmentPageNavigator by lifecycleBound(
            creator = {
                FragmentPageNavigator(
                        fragmentManager = childFragmentManager
                )
            }
    )

    private val selectedProductPage by viewComponent {
        SelectedProductPageViewComponent(view as ViewGroup, object : SelectedProductPageViewComponent.Listener {
            override fun onProductSelectStateChanged(productId: Long, isSelected: Boolean) {
                viewModel.selectProduct(productId, isSelected)
                onSelectedProductChanged()
            }
        })
    }

    private val bottomActionView by viewComponent {
        BottomActionViewComponent(view as ViewGroup, object : BottomActionViewComponent.Listener {
            override fun onInventoryIconClicked() {
                showSelectedProductPage()
                analytic.clickSelectedProductIcon()
            }

            override fun onNextButtonClicked() {
                uploadProduct()
            }
        })
    }

    private var mListener: Listener? = null

    private var toasterBottomMargin = 0

    private val fragmentFactory: FragmentFactory
        get() = childFragmentManager.fragmentFactory

    private val currentFragment: Fragment?
        get() = childFragmentManager.findFragmentById(R.id.fl_etalase_flow)

    override fun getScreenName(): String = "Play Etalase Picker"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupTransition()
        viewModel = ViewModelProviders.of(requireParentFragment(), viewModelFactory).get(PlayEtalasePickerViewModel::class.java)
        dataStoreViewModel = ViewModelProviders.of(this, viewModelFactory).get(DataStoreViewModel::class.java)
    }

    override fun onStart() {
        super.onStart()
        analytic.viewProductBottomSheet()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        postponeEnterTransition()
        return inflater.inflate(R.layout.fragment_play_etalase_picker, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        setupView(view)

        showBottomAction(false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        observeSelectedProducts()
        observeUploadProduct()
    }

    override fun onPause() {
        psbSearch.clearFocus()
        super.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        job.cancelChildren()
    }

    override fun onInterceptBackPressed(): Boolean {
        return when {
            selectedProductPage.isShown() -> {
                selectedProductPage.hide()
                true
            }
            currentFragment !is PlayEtalaseListFragment -> {
                psbSearch.cancel()
                true
            }
            else -> false
        }
    }

    override fun openEtalaseDetail(etalaseId: String, etalaseName: String, sharedElements: List<View>) {
        mListener?.onEtalaseClicked(etalaseId, sharedElements)
        analytic.clickEtalase(etalaseName)
    }

    override fun openSearchPage(keyword: String) {
        openFragment(
                PlaySearchSuggestionsFragment::class.java,
                extras = Bundle().apply {
                    putString(PlaySearchSuggestionsFragment.EXTRA_KEYWORD, keyword)
                }
        )
    }

    override fun openProductSearchPage(keyword: String) {
        openFragment(
                PlaySearchResultFragment::class.java,
                extras = Bundle().apply {
                    putString(PlaySearchResultFragment.EXTRA_KEYWORD, keyword)
                }
        )

        psbSearch.clearFocus()
        psbSearch.text = keyword
    }

    override fun goBack(clazz: Class<out Fragment>) {
        if (childFragmentManager.fragments.isNotEmpty()) childFragmentManager.popBackStack(clazz.name, 0)
    }

    override fun getParent(): Fragment {
        return requireParentFragment()
    }

    fun setListener(listener: Listener) {
        mListener = listener
    }

    private fun initView(view: View) {
        with(view) {
            container = this as ViewGroup
            tvInfo = findViewById(R.id.tv_info)
            flEtalaseFlow = findViewById(R.id.fl_etalase_flow)
            psbSearch = findViewById(R.id.psb_search)
            errorEtalase = findViewById(R.id.error_etalase)
            bottomSheetHeader = findViewById(R.id.bottom_sheet_header)
        }
    }

    private fun setupView(view: View) {
        psbSearch.setListener(object : PlaySearchBar.Listener {

            override fun onEditStateChanged(view: PlaySearchBar, isEditing: Boolean) {
                if (isEditing) {
                    openSearchPage(view.text)
                    analytic.clickSearchBar(view.text)
                }
            }

            override fun onCanceled(view: PlaySearchBar) {
                goBack(PlayEtalaseListFragment::class.java)
            }

            override fun onNewKeyword(view: PlaySearchBar, keyword: String) {
                val currFragment = currentFragment
                if (currFragment is PlaySearchSuggestionsFragment) currFragment.searchKeyword(keyword)
            }

            override fun onSearchButtonClicked(view: PlaySearchBar, keyword: String) {
                if (keyword.isNotEmpty()) {
                    openProductSearchPage(keyword)
                    analytic.clickSearchBar(view.text)
                }
                else psbSearch.cancel()
            }
        })

        if (currentFragment == null) openFragment(PlayEtalaseListFragment::class.java)

        bottomSheetHeader.setListener(object : PlayBottomSheetHeader.Listener {
            override fun onBackButtonClicked(view: PlayBottomSheetHeader) {
                bottomSheetCoordinator.goBack()
            }
        })
        bottomSheetHeader.setHeader(getString(R.string.play_etalase_picker_title), isRoot = true)

        errorEtalase.setActionClickListener { viewModel.loadEtalaseList() }
    }


    private fun openFragment(
            fragmentClass: Class<out Fragment>,
            extras: Bundle = Bundle.EMPTY,
            sharedElements: List<View> = emptyList(),
    ) {
        pageNavigator.navigate(
                flEtalaseFlow.id,
                fragmentClass,
                extras,
                sharedElements
        )
    }

    private fun showSelectedProductPage() {
        if (selectedProductPage.isShown()) {
            selectedProductPage.hide()
        } else {
            selectedProductPage.setSelectedProductList(viewModel.selectedProductList)
            selectedProductPage.show()
        }
    }

    override fun showBottomAction(shouldShow: Boolean) {
        if (shouldShow) bottomActionView.show() else bottomActionView.hide()
    }

    override fun showGlobalError(errorType: Int, errorAction: () -> Unit) {
        errorEtalase.setType(errorType)
        errorEtalase.setActionClickListener { errorAction() }
        errorEtalase.show()

        tvInfo.hide()
        psbSearch.hide()
        flEtalaseFlow.hide()

        analytic.viewEtalaseError(errorEtalase.errorTitle.text.toString())
    }

    override fun hideGlobalError() {
        errorEtalase.hide()

        tvInfo.show()
        psbSearch.show()
        flEtalaseFlow.show()
    }

    override fun showToaster(message: String, type: Int, duration: Int, actionLabel: String, actionListener: View.OnClickListener) {
        if (toasterBottomMargin == 0) {
            val offset8 = resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3)
            toasterBottomMargin = bottomActionView.rootView.height + offset8
        }

        view?.showToaster(
                message = message,
                type = type,
                duration = duration,
                actionLabel = actionLabel,
                actionListener = actionListener,
                bottomMargin = toasterBottomMargin
        )
    }

    private fun onSelectedProductChanged() {
        (currentFragment as? PlayBaseEtalaseSetupFragment)?.refresh()
    }

    private fun uploadProduct() {
        viewModel.uploadProduct()
    }

    private fun onUploadSuccess() {
        scope.launch {
            val error = mListener?.onProductSetupFinished(listOf(bottomActionView.getButtonView()), dataStoreViewModel.getDataStore())
            if (error != null) {
                yield()
                onUploadFailed(error)
            }
        }
    }

    private fun onUploadFailed(e: Throwable?) {
        bottomActionView.setLoading(false)
        e?.localizedMessage?.let {
            errMessage -> showToaster(errMessage, type = Toaster.TYPE_ERROR)
        }
    }

    /**
     * Observe
     */
    private fun observeSelectedProducts() {
        viewModel.observableSelectedProducts.observe(viewLifecycleOwner, Observer {
            bottomActionView.setupBottomActionWithProducts(it)
            selectedProductPage.onSelectedProductsUpdated(it)
        })
    }

    private fun observeUploadProduct() {
        viewModel.observableUploadProductEvent.observe(viewLifecycleOwner, Observer {
            when (it) {
                NetworkResult.Loading -> {
                    bottomActionView.setLoading(true)
                }
                is NetworkResult.Fail -> onUploadFailed(it.error)
                is NetworkResult.Success -> {
                    val data = it.data.getContentIfNotHandled()
                    if (data != null) onUploadSuccess()
                }
            }
        })
    }

    /**
     * Transition
     */
    private fun setupTransition() {
        setupExitTransition()
        setupReenterTransition()
    }

    private fun setupExitTransition() {
        exitTransition = TransitionSet()
                .addTransition(Slide(Gravity.START))
                .addTransition(Fade(Fade.OUT))
                .setDuration(300)
    }

    private fun setupReenterTransition() {
        reenterTransition = TransitionSet()
                .addTransition(Slide(Gravity.START))
                .addTransition(Fade(Fade.IN))
                .setDuration(300)
    }

    interface Listener : ProductSetupListener {

        fun onEtalaseClicked(id: String, sharedElements: List<View>)
    }
}