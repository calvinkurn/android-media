package com.tokopedia.scp_rewards.cabinet.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.applink.internal.ApplinkConstInternalPromo.MEDALI_QUERY_PARAM
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.scp_rewards.R
import com.tokopedia.scp_rewards.cabinet.analytics.MedalCabinetAnalyticsImpl
import com.tokopedia.scp_rewards.cabinet.di.MedalCabinetComponent
import com.tokopedia.scp_rewards.cabinet.domain.model.ScpRewardsGetUserMedalisResponse
import com.tokopedia.scp_rewards.cabinet.mappers.MedaliListMapper
import com.tokopedia.scp_rewards.cabinet.presentation.adapter.SeeMoreMedalAdapter
import com.tokopedia.scp_rewards.cabinet.presentation.viewmodel.SeeMoreMedaliViewModel
import com.tokopedia.scp_rewards.common.data.Error
import com.tokopedia.scp_rewards.common.data.Loading
import com.tokopedia.scp_rewards.common.data.Success
import com.tokopedia.scp_rewards.databinding.SeeMoreMedaliFragmentBinding
import com.tokopedia.scp_rewards_common.EARNED_BADGE
import com.tokopedia.scp_rewards_widgets.common.GridSpacing
import com.tokopedia.scp_rewards_widgets.common.model.LoadingModel
import com.tokopedia.scp_rewards_widgets.common.model.LoadingMoreModel
import com.tokopedia.scp_rewards_widgets.medal.MedalCallbackListener
import com.tokopedia.scp_rewards_widgets.medal.MedalData
import com.tokopedia.scp_rewards_widgets.medal.MedalItem
import com.tokopedia.scp_rewards_widgets.medal.MedalViewHolder
import com.tokopedia.scp_rewards_widgets.medal.SeeMoreMedalTypeFactory
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class SeeMoreMedaliFragment : BaseDaggerFragment(), MedalCallbackListener {

    companion object {
        private const val FULL_COLUMN = 1
        private const val ONE_THIRD_COLUMN = 3
        private const val GRID_VERTICAL_SPACING = 20
        private const val GRID_HORIZONTAL_SPACING = 20
        private const val COLUMN_3 = 3
    }

    private var binding: SeeMoreMedaliFragmentBinding? = null

    private val medalAdapter: SeeMoreMedalAdapter by lazy {
        SeeMoreMedalAdapter(SeeMoreMedalTypeFactory(this))
    }

    private var badgeType = ""

    private var rvScrollListener: EndlessRecyclerViewScrollListener? = null

    @Inject
    @JvmField
    var viewModelFactory: ViewModelFactory? = null

    private val viewModel: SeeMoreMedaliViewModel by lazy {
        ViewModelProvider(this, viewModelFactory!!)[SeeMoreMedaliViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        badgeType = activity?.intent?.data?.getQueryParameter(MEDALI_QUERY_PARAM) ?: EARNED_BADGE
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SeeMoreMedaliFragmentBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getUserMedalis(badgeType = badgeType)
        setupHeader()
        setupRecyclerView()
        setupViewModelObservers()
    }

    private fun setupHeader() {
        binding?.header?.apply {
            title = getHeaderTitle()
            setNavigationOnClickListener {
                if (badgeType == EARNED_BADGE) {
                    MedalCabinetAnalyticsImpl.sendClickBackSeeMoreUnlockedMedalEvent()
                } else {
                    MedalCabinetAnalyticsImpl.sendClickBackSeeMoreLockedMedalEvent()
                }
                activity?.onBackPressed()
            }
        }
    }

    private fun getHeaderTitle(): String {
        return if (badgeType == EARNED_BADGE) {
            MedalCabinetAnalyticsImpl.sendViewSeeMoreUnlockedMedalPageEvent()
            context?.getString(R.string.earned_medali_title) ?: ""
        } else {
            MedalCabinetAnalyticsImpl.sendViewSeeMoreLockedMedalPageEvent()
            context?.getString(R.string.progress_medali_title) ?: ""
        }
    }

    private fun setupViewModelObservers() {
        viewModel.medalLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Success<*> -> {
                    onSuccessResponse(it.data as ScpRewardsGetUserMedalisResponse)
                }

                is Loading -> {
                    onLoadingState()
                }

                is Error -> {
                    onErrorState(it)
                }
            }
        }
        viewModel.hasNextLiveData.observe(viewLifecycleOwner) {
            if (!it) {
                removeRecyclerViewScrollListener()
            }
        }
    }

    private fun onErrorState(result: Error) {
        with(viewModel) {
            if (pageCount == 1) {
                handleError(result.error)
            } else {
                if (visitableList.last() is LoadingMoreModel) {
                    visitableList.removeLast()
                }
            }
        }
    }

    private fun handleError(error: Throwable) {
        binding?.let {
            with(it) {
                badgeRv.gone()
                viewError.visible()
                if (error is UnknownHostException || error is SocketTimeoutException) {
                    viewError.setType(GlobalError.NO_CONNECTION)
                } else {
                    viewError.apply {
                        setType(GlobalError.SERVER_ERROR)
                        errorSecondaryAction.text = context.getText(R.string.goto_main_page_text)
                        setSecondaryActionClickListener {
                            activity?.finish()
                        }
                    }
                }
            }
        }
    }

    private fun onSuccessResponse(response: ScpRewardsGetUserMedalisResponse) {
        with(viewModel) {
            if (pageCount == 1) {
                visitableList.clear()
            } else {
                if (visitableList.last() is LoadingMoreModel) {
                    visitableList.removeLast()
                }
            }
            visitableList.addAll(
                MedaliListMapper.getMedalList(response, badgeType)
            )
            submitAdapterList(visitableList)
            binding?.viewError?.gone()
            binding?.badgeRv?.visible()
            rvScrollListener?.updateStateAfterGetData()
        }
    }

    private fun onLoadingState() {
        with(viewModel) {
            if (pageCount == 1) {
                visitableList.clear()
                visitableList.add(LoadingModel())
            } else {
                viewModel.visitableList.add(LoadingMoreModel())
            }
            submitAdapterList(visitableList)
        }
    }

    private fun submitAdapterList(list: List<Visitable<*>>) {
        medalAdapter.submitList(list)
    }

    private fun setupRecyclerView() {
        var rvLayoutManager: LayoutManager
        binding?.badgeRv?.apply {
            rvLayoutManager = setupLayoutManager()
            setupRecyclerViewScrollListener(rvLayoutManager)
            adapter = medalAdapter
            layoutManager = rvLayoutManager
            addOnScrollListener(rvScrollListener!!)
            addItemDecoration(GridSpacing(GRID_HORIZONTAL_SPACING, GRID_VERTICAL_SPACING))
        }
    }

    private fun setupLayoutManager(): GridLayoutManager {
        return GridLayoutManager(context, COLUMN_3).apply {
            spanSizeLookup = getSpanLookup()
        }
    }

    private fun setupRecyclerViewScrollListener(layoutManager: LayoutManager) {
        rvScrollListener = object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                viewModel.getUserMedalis(page, badgeType)
            }
        }
    }

    private fun getSpanLookup() = object : GridLayoutManager.SpanSizeLookup() {
        override fun getSpanSize(position: Int): Int {
            return when (medalAdapter.getItemViewType(position)) {
                MedalViewHolder.LAYOUT -> FULL_COLUMN
                else -> ONE_THIRD_COLUMN
            }
        }
    }

    private fun removeRecyclerViewScrollListener() {
        if (rvScrollListener != null) {
            binding?.badgeRv?.removeOnScrollListener(rvScrollListener!!)
        }
    }

    override fun initInjector() {
        getComponent(MedalCabinetComponent::class.java).inject(this)
    }

    override fun getScreenName() = ""
    override fun onMedalClick(medalItem: MedalItem) {
        if (medalItem.isEarned()) {
            MedalCabinetAnalyticsImpl.sendClickSeeMoreUnlockedMedalEvent(
                medalItem.id.toString(),
                medalItem.isDisabled ?: false,
                medalItem.name.orEmpty(),
                medalItem.extraInfo.orEmpty()
            )
        } else {
            MedalCabinetAnalyticsImpl.sendClickSeeMoreLockedMedalEvent(
                medalItem.id.toString(),
                medalItem.isDisabled ?: false,
                medalItem.name.orEmpty(),
                medalItem.extraInfo.orEmpty(),
                medalItem.progression.toString()
            )
        }
    }

    override fun onSeeMoreClick(medalData: MedalData) {
        TODO("Not yet implemented")
    }

    override fun onMedalLoad(medalItem: MedalItem) {
        if (medalItem.isEarned()) {
            MedalCabinetAnalyticsImpl.sendViewSeeMoreUnlockedMedalEvent(
                medalItem.id.toString(),
                medalItem.isDisabled ?: false,
                medalItem.name.orEmpty(),
                medalItem.extraInfo.orEmpty()
            )
        } else {
            MedalCabinetAnalyticsImpl.sendViewSeeMoreLockedMedalEvent(
                medalItem.id.toString(),
                medalItem.isDisabled ?: false,
                medalItem.name.orEmpty(),
                medalItem.extraInfo.orEmpty(),
                medalItem.progression.toString()
            )
        }
    }

    override fun onMedalFailed(medalItem: MedalItem) {
        TODO("Not yet implemented")
    }

    override fun onSeeMoreLoad(medalData: MedalData) {
        TODO("Not yet implemented")
    }
}
