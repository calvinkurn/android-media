package com.tokopedia.content.product.preview.utils

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.content.product.preview.view.uimodel.LikeUiState
import com.tokopedia.content.product.preview.view.uimodel.MenuStatus

/**
 * @author by astidhiyaa on 15/12/23
 */
class ReviewResultContract : ActivityResultContract<LikeUiState, LikeUiState>() {
    override fun createIntent(context: Context, input: LikeUiState): Intent {
        return RouteManager.getIntent(context, ApplinkConst.LOGIN).apply {
            putExtra(LIKE, input)
        }
    }

    override fun parseResult(resultCode: Int, intent: Intent?): LikeUiState {
        return intent?.extras?.getParcelable(LIKE) ?: LikeUiState(0, LikeUiState.LikeStatus.Reset)  //TODO: setup default empty
    }

    companion object {
        private const val LIKE = "LIKE_STATE_PARAM"
    }
}

class MenuReviewResultContract : ActivityResultContract<MenuStatus, MenuStatus>() {
    override fun createIntent(context: Context, input: MenuStatus): Intent {
        return RouteManager.getIntent(context, ApplinkConst.LOGIN).apply {
            putExtra(MENU, input)
        }
    }

    override fun parseResult(resultCode: Int, intent: Intent?): MenuStatus {
        return intent?.extras?.getParcelable(MENU) ?: MenuStatus(isReportable = false)  //TODO: setup default empty
    }

    companion object {
        private const val MENU = "MENU_STATUS_PARAM"
    }
}
