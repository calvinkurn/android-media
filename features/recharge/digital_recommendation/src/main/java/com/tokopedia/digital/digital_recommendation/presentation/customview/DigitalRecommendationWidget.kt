package com.tokopedia.digital.digital_recommendation.presentation.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.digital.digital_recommendation.databinding.LayoutDigitalRecommendationBinding
import com.tokopedia.digital.digital_recommendation.presentation.adapter.DigitalRecommendationAdapter
import com.tokopedia.digital.digital_recommendation.presentation.viewmodel.DigitalRecommendationViewModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success

/**
 * @author by furqan on 20/09/2021
 */
class DigitalRecommendationWidget @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : ConstraintLayout(context, attrs, defStyleAttr) {

    private var binding: LayoutDigitalRecommendationBinding =
            LayoutDigitalRecommendationBinding.inflate(
                    LayoutInflater.from(context), this, true
            )

    var listener: Listener? = null

    private lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var lifecycleOwner: LifecycleOwner

    private lateinit var digitalRecommendationViewModel: DigitalRecommendationViewModel

    init {
        showLoading()
    }

    fun setViewModelFactory(viewModelFactory: ViewModelProvider.Factory) {
        this.viewModelFactory = viewModelFactory
        digitalRecommendationViewModel = ViewModelProvider(context as Fragment, viewModelFactory)
                .get(DigitalRecommendationViewModel::class.java)
    }

    fun setLifecycleOwner(lifecycleOwner: LifecycleOwner) {
        this.lifecycleOwner = lifecycleOwner
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
        digitalRecommendationViewModel.fetchDigitalRecommendation()
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
        digitalRecommendationViewModel.digitalRecommendationItems.observe(lifecycleOwner, {
            when (it) {
                is Success -> {
                    hideLoading()

                    with(binding) {
                        tgDigitalRecommendationTitle.show()
                        rvDigitalRecommendation.layoutManager = LinearLayoutManager(context,
                                LinearLayoutManager.HORIZONTAL, false)
                        rvDigitalRecommendation.adapter = DigitalRecommendationAdapter(it.data)
                        rvDigitalRecommendation.show()
                    }
                }
                is Fail -> {
                    binding.root.hide()
                    listener?.onFetchFailed(it.throwable)
                }
            }
        })
    }

    interface Listener {
        fun onFetchFailed(throwable: Throwable)
    }

}