package com.tokopedia.universal_sharing.view.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.universal_sharing.R
import com.tokopedia.universal_sharing.databinding.ActivityShareIsNotFoundBinding
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.utils.view.binding.viewBinding

class ShareNotFoundActivity : BaseActivity() {

    var binding: ActivityShareIsNotFoundBinding? by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share_is_not_found)
        initView()
    }

    private fun initView() {
        binding?.globalError?.errorTitle?.text = getString(R.string.title_update_app)
        binding?.globalError?.errorDescription?.text = getString(R.string.description_error_not_found)
        binding?.globalError?.setActionClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(CUSTOMER_APP_PLAY_STORE)))
        }
    }

    companion object {
        private const val CUSTOMER_APP_PACKAGE = "com.tokopedia.tkpd"
        private const val CUSTOMER_APP_PLAY_STORE = "https://play.google.com/store/apps/details?id=$CUSTOMER_APP_PACKAGE";
    }


}
