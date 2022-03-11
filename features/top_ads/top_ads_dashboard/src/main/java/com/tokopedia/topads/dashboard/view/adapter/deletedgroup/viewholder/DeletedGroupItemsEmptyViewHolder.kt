package com.tokopedia.topads.dashboard.view.adapter.deletedgroup.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tkpd.remoteresourcerequest.view.DeferredImageView
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.media.loader.loadImage
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.view.adapter.deletedgroup.viewmodel.DeletedGroupItemsEmptyModel
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

private const val EMPTY_DELETED_ADS_IMAGE_NAME = "topads_deleted_ads_empty_state"
private const val EMPTY_DELETED_ADS_IMAGE_URL =
    "https://images.tokopedia.net/img/android/res/singleDpi/topads_deleted_ads_empty_state.png"

class DeletedGroupItemsEmptyViewHolder(val view: View) :
    DeletedGroupItemsViewHolder<DeletedGroupItemsEmptyModel>(view) {

    private val btnSubmit: UnifyButton = view.findViewById(R.id.btn_submit)
    private val textTitle: Typography = view.findViewById(R.id.text_title)
    private val textDesc: Typography = view.findViewById(R.id.text_desc)
    private val imageEmpty: DeferredImageView = view.findViewById(R.id.image_empty)

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.topads_dash_deleted_group_empty_state
    }

    override fun bind(item: DeletedGroupItemsEmptyModel) {
        item.let {
            btnSubmit.visibility = View.GONE
            textTitle.text =
                view.context.getString(R.string.topads_dash_deleted_group_empty_result_title)
            textDesc.text = view.context.getString(R.string.topads_deleted_empty_ads_desc)
            imageEmpty.loadImage(view.context.getResDrawable(com.tokopedia.topads.common.R.drawable.no_products))
            imageEmpty.loadRemoteImageDrawable(
                EMPTY_DELETED_ADS_IMAGE_NAME,
                EMPTY_DELETED_ADS_IMAGE_URL
            )
        }
    }
}