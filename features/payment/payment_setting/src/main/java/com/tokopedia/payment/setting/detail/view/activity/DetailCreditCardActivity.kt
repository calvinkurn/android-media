package com.tokopedia.payment.setting.detail.view.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.payment.setting.detail.view.fragment.DetailCreditCardFragment
import com.tokopedia.payment.setting.list.model.SettingListPaymentModel

class DetailCreditCardActivity : BaseSimpleActivity(){

    override fun getNewFragment(): Fragment {
        val settingListPaymentModel = intent?.extras?.getParcelable<SettingListPaymentModel>(DetailCreditCardFragment.EXTRA_PAYMENT_MODEL)
        return DetailCreditCardFragment.createInstance(settingListPaymentModel)
    }

    companion object {
        fun createIntent(context: Context, settingListPaymentModel: SettingListPaymentModel?) : Intent{
            val intent = Intent(context, DetailCreditCardActivity::class.java)
            intent.putExtra(DetailCreditCardFragment.EXTRA_PAYMENT_MODEL, settingListPaymentModel)
            return intent
        }
    }
}