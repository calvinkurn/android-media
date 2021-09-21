package com.tokopedia.pms.clickbca.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.config.GlobalConfig
import com.tokopedia.pms.paymentlist.di.DaggerPmsComponent
import com.tokopedia.pms.paymentlist.di.PmsComponent

/**
 * Created by zulfikarrahman on 6/25/18.
 */
class ChangeClickBcaActivity : BaseSimpleActivity(), HasComponent<PmsComponent> {

    private lateinit var component: PmsComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSecureWindowFlag()
    }

    private fun setSecureWindowFlag() {
        if (GlobalConfig.APPLICATION_TYPE == GlobalConfig.CONSUMER_APPLICATION || GlobalConfig.APPLICATION_TYPE == GlobalConfig.SELLER_APPLICATION) {
            runOnUiThread {
                val window = window
                window?.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
            }
        }
    }

    override fun getNewFragment(): Fragment {
        val transactionId = intent.getStringExtra(TRANSACTION_ID)
        val merchantCode = intent.getStringExtra(MERCHANT_CODE)
        val userIdKlikBca = intent.getStringExtra(USER_ID_KLIK_BCA)
        return ChangeClickBcaFragment.createInstance(transactionId, merchantCode, userIdKlikBca)
    }

    companion object {
        const val TRANSACTION_ID = "transactionID"
        const val MERCHANT_CODE = "merchantCode"
        const val USER_ID_KLIK_BCA = "user_id_klik_bca"

        fun createIntent(
            context: Context?,
            transactionId: String?,
            merchantCode: String?,
            userIdKlikBca: String?
        ): Intent {
            val intent = Intent(context, ChangeClickBcaActivity::class.java)
            intent.putExtra(TRANSACTION_ID, transactionId)
            intent.putExtra(MERCHANT_CODE, merchantCode)
            intent.putExtra(USER_ID_KLIK_BCA, userIdKlikBca)
            return intent
        }
    }

    override fun getComponent(): PmsComponent {
        if (!::component.isInitialized)
            component = DaggerPmsComponent.builder()
                .baseAppComponent(
                    (applicationContext as BaseMainApplication)
                        .baseAppComponent
                ).build()
        return component
    }
}