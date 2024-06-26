package com.tokopedia.digital.digital_recommendation.presentation.widget

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.applink.RouteManager
import com.tokopedia.digital.digital_recommendation.databinding.LayoutDigitalRecommendationBinding
import com.tokopedia.digital.digital_recommendation.presentation.model.DigitalRecommendationAdditionalTrackingData
import com.tokopedia.digital.digital_recommendation.presentation.model.DigitalRecommendationItemUnifyModel
import com.tokopedia.digital.digital_recommendation.presentation.model.DigitalRecommendationModel
import com.tokopedia.digital.digital_recommendation.presentation.model.DigitalRecommendationPage
import com.tokopedia.digital.digital_recommendation.presentation.viewmodel.DigitalRecommendationViewModel
import com.tokopedia.digital.digital_recommendation.utils.DigitalRecommendationAnalytics
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.recharge_component.digital_card.presentation.adapter.DigitalUnifyCardAdapterTypeFactory
import com.tokopedia.recharge_component.digital_card.presentation.adapter.viewholder.DigitalUnifyCardViewHolder
import com.tokopedia.recharge_component.digital_card.presentation.model.DigitalUnifyModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success

/**
 * @author by furqan on 20/09/2021
 */
class DigitalRecommendationWidget @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    ConstraintLayout(context, attrs, defStyleAttr) {

    private var binding: LayoutDigitalRecommendationBinding =
        LayoutDigitalRecommendationBinding.inflate(
            LayoutInflater.from(context),
            this,
            true
        )

    var listener: Listener? = null

    private lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var lifecycleOwner: LifecycleOwner

    private lateinit var digitalRecommendationViewModel: DigitalRecommendationViewModel

    private var additionalTrackingData: DigitalRecommendationAdditionalTrackingData? = null
    private var page: DigitalRecommendationPage? = null
    private var trackers: List<DigitalRecommendationItemUnifyModel>? = null

    private val digitalRecommendationAnalytics: DigitalRecommendationAnalytics by lazy(LazyThreadSafetyMode.NONE) { DigitalRecommendationAnalytics() }

    private val unifyListener = object : DigitalUnifyCardViewHolder.DigitalUnifyCardListener {
        override fun onItemClicked(item: DigitalUnifyModel, index: Int) {
            trackers.getElementByIndex(index) { onItemClicked(it, index) }
        }

        override fun onItemImpression(item: DigitalUnifyModel, index: Int) {
            trackers.getElementByIndex(index) { onItemBinding(it, index) }
        }
    }

    private lateinit var adapter: BaseAdapter<DigitalUnifyCardAdapterTypeFactory>

    init {
        showLoading()
    }

    override fun onSaveInstanceState(): Parcelable {
        super.onSaveInstanceState()
        return Bundle().apply {
            putParcelable(SAVED_ADDITIONAL_TRACK_DATA, additionalTrackingData)
            putSerializable(SAVED_PAGE, page)
            putParcelable(SUPER_STATE, super.onSaveInstanceState())
        }
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        var viewState = state
        if (viewState is Bundle) {
            additionalTrackingData = viewState.getParcelable(SAVED_ADDITIONAL_TRACK_DATA)
            page = viewState.getSerializable(SAVED_PAGE) as DigitalRecommendationPage
            viewState = viewState.getParcelable(SUPER_STATE)
        }
        super.onRestoreInstanceState(viewState)
    }

    private fun onItemBinding(element: DigitalRecommendationItemUnifyModel, position: Int) {
        additionalTrackingData?.let {
            when (page) {
                DigitalRecommendationPage.PG_THANK_YOU_PAGE -> {
                    digitalRecommendationAnalytics.impressionDigitalRecommendationThankYouPageItems(
                        element,
                        it,
                        position,
                        digitalRecommendationViewModel.getUserId(),
                        page
                    )
                }
                DigitalRecommendationPage.DG_THANK_YOU_PAGE -> {
                    digitalRecommendationAnalytics.impressionDigitalRecommendationThankYouPageItems(
                        element,
                        it,
                        position,
                        digitalRecommendationViewModel.getUserId(),
                        page
                    )
                }
                DigitalRecommendationPage.PHYSICAL_GOODS -> {
                    digitalRecommendationAnalytics.impressionDigitalRecommendationItems(
                        element,
                        it,
                        position,
                        digitalRecommendationViewModel.getUserId(),
                        page
                    )
                }
                DigitalRecommendationPage.DIGITAL_GOODS -> {
                    digitalRecommendationAnalytics.impressionDigitalRecommendationItems(
                        element,
                        it,
                        position,
                        digitalRecommendationViewModel.getUserId(),
                        page
                    )
                }
                else -> { /*no op*/ }
            }
        }
    }

    private fun onItemClicked(element: DigitalRecommendationItemUnifyModel, position: Int) {
        RouteManager.route(context, element.unify.actionButton.applink)
        additionalTrackingData?.let {
            when (page) {
                DigitalRecommendationPage.PG_THANK_YOU_PAGE -> {
                    digitalRecommendationAnalytics.clickDigitalRecommendationThankYouPageItem(
                        element,
                        it,
                        position,
                        digitalRecommendationViewModel.getUserId(),
                        page
                    )
                }
                DigitalRecommendationPage.DG_THANK_YOU_PAGE -> {
                    digitalRecommendationAnalytics.clickDigitalRecommendationThankYouPageItem(
                        element,
                        it,
                        position,
                        digitalRecommendationViewModel.getUserId(),
                        page
                    )
                }
                DigitalRecommendationPage.PHYSICAL_GOODS -> {
                    digitalRecommendationAnalytics.clickDigitalRecommendationItems(
                        element,
                        it,
                        position,
                        digitalRecommendationViewModel.getUserId(),
                        page
                    )
                }
                DigitalRecommendationPage.DIGITAL_GOODS -> {
                    digitalRecommendationAnalytics.clickDigitalRecommendationItems(
                        element,
                        it,
                        position,
                        digitalRecommendationViewModel.getUserId(),
                        page
                    )
                }
                else -> { /*no op*/ }
            }
        }
    }

    fun setViewModelFactory(viewModelFactory: ViewModelProvider.Factory) {
        this.viewModelFactory = viewModelFactory
        digitalRecommendationViewModel = ViewModelProvider(context as AppCompatActivity, viewModelFactory)
            .get(DigitalRecommendationViewModel::class.java)
    }

    fun setLifecycleOwner(lifecycleOwner: LifecycleOwner) {
        this.lifecycleOwner = lifecycleOwner
    }

    fun setAdditionalData(additionalTrackingData: DigitalRecommendationAdditionalTrackingData) {
        this.additionalTrackingData = additionalTrackingData
    }

    fun setPage(page: DigitalRecommendationPage) {
        this.page = page
    }

    fun build() {
        if (!::viewModelFactory.isInitialized) {
            throw UninitializedPropertyAccessException("View Model Factory is not Initialized")
        }

        if (!::lifecycleOwner.isInitialized) {
            throw UninitializedPropertyAccessException("Lifecycle Owner is not Initialized")
        }

        if (!::digitalRecommendationViewModel.isInitialized) {
            throw UninitializedPropertyAccessException("View Model is not Initialized")
        }

        showLoading()
        observeLivedata()
        digitalRecommendationViewModel.fetchDigitalRecommendation(
            page ?: DigitalRecommendationPage.DIGITAL_GOODS,
            additionalTrackingData?.dgCategories ?: emptyList(),
            additionalTrackingData?.pgCategories ?: emptyList()
        )
    }

    fun showLoading() {
        binding.loadingDigitalRecommendation.root.show()
        binding.tgDigitalRecommendationTitle.hide()
        binding.rvDigitalRecommendation.hide()
    }

    fun hideLoading() {
        binding.loadingDigitalRecommendation.root.hide()
    }

    private fun observeLivedata() {
        digitalRecommendationViewModel.digitalRecommendationItems.observe(
            lifecycleOwner,
            Observer<Result<DigitalRecommendationModel>> {
                when (it) {
                    is Success -> {
                        if (it.data.items.isNotEmpty()) {
                            hideLoading()

                            if (!::adapter.isInitialized) {
                                additionalTrackingData?.userType = it.data.userType
                                trackers = it.data.items
                                adapter = BaseAdapter(
                                    DigitalUnifyCardAdapterTypeFactory(unifyListener),
                                    it.data.items.map { item -> item.unify }
                                )
                            }

                            with(binding) {
                                tgDigitalRecommendationTitle.show()
                                tgDigitalRecommendationTitle.text = it.data.title
                                rvDigitalRecommendation.layoutManager = LinearLayoutManager(
                                    context,
                                    LinearLayoutManager.HORIZONTAL,
                                    false
                                )
                                rvDigitalRecommendation.adapter = adapter
                                rvDigitalRecommendation.show()
                            }
                        } else {
                            listener?.onEmptyResult()
                        }
                    }
                    is Fail -> {
                        binding.root.hide()
                        listener?.onFetchFailed(it.throwable)
                    }
                }
            }
        )
    }

    private inline fun List<DigitalRecommendationItemUnifyModel>?.getElementByIndex(
        index: Int,
        block: (DigitalRecommendationItemUnifyModel) -> Unit
    ) {
        if (this != null && index >= 0 && index <= size - MAX_INDEX_SUBTRACTOR) {
            block(this[index])
        }
    }

    interface Listener {
        fun onFetchFailed(throwable: Throwable)
        fun onEmptyResult()
    }

    companion object {
        private const val SAVED_ADDITIONAL_TRACK_DATA = "SAVED_ADDITIONAL_TRACK_DATA"
        private const val SAVED_PAGE = "SAVED_PAGE"
        private const val SUPER_STATE = "superState"
        private const val MAX_INDEX_SUBTRACTOR = 1
    }
}
