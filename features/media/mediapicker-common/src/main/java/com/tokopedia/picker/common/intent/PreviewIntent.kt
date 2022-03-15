package com.tokopedia.picker.common.intent

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.fragment.app.FragmentActivity
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMedia.INTERNAL_MEDIA_PICKER_PREVIEW
import com.tokopedia.picker.common.uimodel.MediaUiModel

const val EXTRA_PREVIEW_ITEMS = "picker-preview-items"
const val RESULT_PREVIEW = "result-preview"

object PreviewIntent {

    object Router {
        fun intent(context: Context, elements: ArrayList<MediaUiModel>): Intent {
            return RouteManager.getIntent(context, INTERNAL_MEDIA_PICKER_PREVIEW).apply {
                putExtra(EXTRA_PREVIEW_ITEMS, elements)
            }
        }

        fun get(intent: Intent?): ArrayList<MediaUiModel> {
            return intent?.getParcelableArrayListExtra(EXTRA_PREVIEW_ITEMS)?: arrayListOf()
        }
    }

    object Result {
        fun finish(fa: FragmentActivity, model: ArrayList<MediaUiModel>) {
            val intent = Intent()
            intent.putExtra(RESULT_PREVIEW, model)
            fa.setResult(Activity.RESULT_OK, intent)
            fa.finish()
        }

        fun get(intent: Intent?): ArrayList<MediaUiModel>? {
            return intent?.getParcelableArrayListExtra(RESULT_PREVIEW)
        }
    }

}