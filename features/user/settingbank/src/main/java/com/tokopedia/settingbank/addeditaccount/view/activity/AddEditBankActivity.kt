package com.tokopedia.settingbank.addeditaccount.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.settingbank.addeditaccount.view.fragment.AddEditBankFormFragment
import com.tokopedia.settingbank.addeditaccount.view.viewmodel.BankFormModel

/**
 * @author by nisie on 6/21/18.
 * * For navigating to this class
 * {@link com.tokopedia.applink.internal.ApplinkConstInternalGlobal#ADD_BANK}
 */
class AddEditBankActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment {
        val bundle = Bundle()
        if (intent.extras != null) {
            bundle.putAll(intent.extras)
        }
        return AddEditBankFormFragment()
    }

    companion object {
        const val PARAM_ACTION = "action"
        const val PARAM_DATA = "data"


        fun createIntentAddBank(context: Context): Intent {
            val PARAM_ACTION = "action"

            var intent = Intent(context, AddEditBankActivity::class.java)
            val bundle = Bundle()
            bundle.putString(PARAM_ACTION, BankFormModel.Companion.STATUS_ADD)
            intent.putExtras(bundle)
            return intent
        }

        fun createIntentEditBank(context: Context, bankFormModel: BankFormModel): Intent {
            val intent = Intent(context, AddEditBankActivity::class.java)
            val bundle = Bundle()
            bundle.putParcelable(PARAM_DATA, bankFormModel)
            bundle.putString(PARAM_ACTION, BankFormModel.Companion.STATUS_EDIT)
            intent.putExtras(bundle)
            return intent
        }
    }

    fun setTitle(title : String){
        toolbar.title = title
    }
}