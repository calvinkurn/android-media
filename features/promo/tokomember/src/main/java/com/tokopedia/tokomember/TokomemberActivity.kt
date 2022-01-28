package com.tokopedia.tokomember

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.tokomember.model.BottomSheetContentItem
import com.tokopedia.user.session.UserSession
import timber.log.Timber

class TokomemberActivity : BaseActivity() {
    var isOnResume = false
    private var appLink : String?=null
    private val REQUEST_CODE_LOGIN = 12
    lateinit var userSession: UserSession

    companion object {

        const val REDIRECTION_LINK = "redirectionLink"
        const val DATA_HASH_CODE = "dataHash"

        fun getIntent(context: Context, redirectionLink: String = "",hashCode:Int = 0 , bundle: Bundle= Bundle()): Intent {
            val intent = Intent(context, TokomemberActivity::class.java)
            intent.putExtra(REDIRECTION_LINK, redirectionLink)
            intent.putExtra(DATA_HASH_CODE,hashCode)
            intent.putExtra("key_membership",bundle)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userSession = UserSession(this)
        handleDimming()
        appLink = intent.extras?.getString(REDIRECTION_LINK, "") ?: ""
        showTmBottomSheetDetail()
    }

    private fun handleDimming() {
        try {
            window.setDimAmount(0f)
        } catch (th: Throwable) {
            Timber.e(th)
        }
    }

    private fun showTmBottomSheetDetail() {
        val bottomSheet = TokomemberBottomSheetView.newInstance(intent?.extras?:Bundle())
        bottomSheet.setShowListener {
            val titleMargin = dpToPx(16).toInt()
            bottomSheet.bottomSheetWrapper.setPadding(0, dpToPx(16).toInt(), 0, 0)
            bottomSheet.bottomSheetTitle.setMargin(titleMargin, 0, 0, 0)
        }
        bottomSheet.setOnDismissListener {
            if (isOnResume) {
                finish()
            }
        }
        bottomSheet.show(supportFragmentManager, "BottomSheet Tag")
    }

    override fun onResume() {
        super.onResume()
        isOnResume = true
    }

    override fun onStop() {
        super.onStop()
        isOnResume = false
    }

    override fun onPause() {
        super.onPause()
        isOnResume = false
    }

    override fun onDestroy() {
        super.onDestroy()
        isOnResume = false
    }
}