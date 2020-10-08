package com.tokopedia.review.feature.ovoincentive.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.review.R
import com.tokopedia.review.feature.ovoincentive.data.ProductRevIncentiveOvoDomain
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.incentive_ovo_bottom_sheet_submitted.*

class IncentiveOvoSubmittedBottomSheet(
        private val productRevIncentiveOvoDomain: ProductRevIncentiveOvoDomain?,
        private val amount: Float
): BottomSheetUnify() {

    companion object {
        val TAG = IncentiveOvoSubmittedBottomSheet::class.qualifiedName
        val layout = R.layout.incentive_ovo_bottom_sheet_submitted
        const val url = "https://ecs7.tokopedia.net/android/others/ovo_incentive_bottom_sheet_image.png"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = View.inflate(context, layout, null)
        initView(view)
        setChild(view)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initView(view: View) {
        view.apply {
            incentiveOvoSubmittedImage.loadImage(url)
            incentiveOvoSubmittedTitle.text = context.getString(R.string.review_create_thank_you_title)
            incentiveOvoSubmittedSubtitle.text = context.getString(R.string.review_create_thank_you_subtitle, amount)
            productRevIncentiveOvoDomain?.let {
                incentiveOvoSendAnother.apply {
                    setOnClickListener {
                        dismiss()
                    }
                }
                incentiveOvoLater.setOnClickListener {
                    dismiss()
                }
            }
        }
        isFullpage = false
        showCloseIcon = false
    }
}