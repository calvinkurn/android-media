package com.tokopedia.sellerreview.view.bottomsheet

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.di.component.DaggerSellerHomeComponent
import com.tokopedia.sellerreview.common.Const
import com.tokopedia.sellerreview.view.viewmodel.SellerReviewViewModel
import kotlinx.android.synthetic.main.sir_feedback_bottom_sheet.view.*

/**
 * Created By @ilhamsuaib on 22/01/21
 */

class FeedbackBottomSheet : BaseBottomSheet() {

    companion object {
        const val TAG = "SirFeedbackBottomSheet"
        private const val KEY_RATING = "key_rating"

        fun createInstance(rating: Int): FeedbackBottomSheet {
            return FeedbackBottomSheet().apply {
                overlayClickDismiss = false
                arguments = Bundle().apply {
                    putInt(KEY_RATING, rating)
                }
            }
        }
    }

    private val mViewModel: SellerReviewViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(SellerReviewViewModel::class.java)
    }
    private var onSubmitted: (() -> Unit)? = null

    override fun getResLayout(): Int = R.layout.sir_feedback_bottom_sheet

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeReviewState()
    }

    override fun initInjector() {
        val baseComponent = (requireContext().applicationContext as BaseMainApplication).baseAppComponent
        DaggerSellerHomeComponent.builder()
                .baseAppComponent(baseComponent)
                .build()
                .inject(this)
    }

    override fun setupView() = childView?.run {
        imgSirFeedback.setImageUrl(Const.IMG_REQUEST_FEEDBACK)
        tauSirFeedback.textAreaInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                btnSirSubmitFeedback.isEnabled = !s.isNullOrBlank()
            }
        })
        btnSirSubmitFeedback.setOnClickListener {
            setOnSubmitClicked()
        }
    }

    override fun show(fm: FragmentManager) {
        show(fm, TAG)
    }

    fun setOnSubmittedListener(action: () -> Unit): FeedbackBottomSheet {
        onSubmitted = action
        return this
    }

    private fun observeReviewState() {
        mViewModel.reviewStatus.observe(viewLifecycleOwner) {
            this.dismiss()
            onSubmitted?.invoke()
        }
    }

    private fun setOnSubmitClicked() = childView?.run {
        btnSirSubmitFeedback.isLoading = true
        val rating = arguments?.getInt(KEY_RATING).orZero()
        val feedback = tauSirFeedback.textAreaInput.text.toString()
        val param = getParams(rating, feedback)
        mViewModel.submitReview(param)
    }
}