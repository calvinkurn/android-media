package com.tokopedia.imagepicker_insta.fragment

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import com.tokopedia.applink.RouteManager
import com.tokopedia.content.common.navigation.shorts.PlayShorts
import com.tokopedia.content.common.navigation.shorts.PlayShortsParam
import com.tokopedia.content.common.types.BundleData
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.imagepicker_insta.R as imagePickerInstaR

/**
 * Created by meyta.taliti on 17/04/23.
 */
class FeedVideoDepreciateBottomSheet : BottomSheetUnify() {

    init {
        showKnob = true
        showHeader = false
        showCloseIcon = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setChild(
            View.inflate(
                requireContext(),
                imagePickerInstaR.layout.feed_video_depreciate_bottomsheet,
                null
            )
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<ImageView>(imagePickerInstaR.id.img_feed_new_video_uploader)
            .loadImage(
                getString(imagePickerInstaR.string.feed_new_video_uploader)
            )

        view.findViewById<UnifyButton>(imagePickerInstaR.id.btn_feed_new_video_uploader)
            .setOnClickListener {
                goToPlayShort()
            }

        view.findViewById<UnifyButton>(imagePickerInstaR.id.btn_feed_ok)
            .setOnClickListener {
                dismiss()
            }
    }

    private fun goToPlayShort() {
        val asBuyer = if (arguments == null) false else {
            requireArguments().getBoolean(BundleData.IS_CREATE_POST_AS_BUYER)
        }
        val appLink = PlayShorts.generateApplink {
            setAuthorType(
                if (asBuyer) PlayShortsParam.AuthorType.User else PlayShortsParam.AuthorType.Shop
            )
        }
        RouteManager.route(requireContext(), appLink)
    }

    companion object {

        fun newInstance(asBuyer: Boolean): FeedVideoDepreciateBottomSheet {
            return FeedVideoDepreciateBottomSheet().apply {
                arguments = Bundle().apply {
                    putBoolean(BundleData.IS_CREATE_POST_AS_BUYER, asBuyer)
                }
            }
        }
    }
}
