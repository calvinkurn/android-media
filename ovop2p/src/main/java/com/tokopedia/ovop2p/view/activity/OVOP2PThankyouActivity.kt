package com.tokopedia.ovop2p.view.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import com.airbnb.deeplinkdispatch.DeepLink
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.ovop2p.Constants
import com.tokopedia.ovop2p.view.fragment.FragmentTransactionSuccessNonOvoUser
import com.tokopedia.ovop2p.view.fragment.FragmentTransactionSuccessOvoUser

class OVOP2PThankyouActivity : BaseSimpleActivity()  {

    private var transferId: String = ""
    private var nonOvoUser: Boolean = false

    object DeeplinkIntents {
        @DeepLink(Constants.AppLinks.OVOP2PTHANKYOUPAGE)
        @JvmStatic
        fun getCallingStartUpgradeToOvo(context: Context, extras: Bundle): Intent {
            val uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon()
            return Intent(context, OVOP2PThankyouActivity::class.java)
                    .setData(uri.build())
                    .putExtras(extras)
        }
    }

    override fun getNewFragment(): Fragment {
        if(intent != null){
            nonOvoUser = intent.extras.getBoolean(Constants.Keys.NON_OVO_SUCS)
            transferId = intent.extras.getString(Constants.PlaceHolders.TRNSFER_ID_PLCHLDR)
            if(nonOvoUser){
                return FragmentTransactionSuccessNonOvoUser.newInstance()
            }
            else{
                if(!TextUtils.isEmpty(transferId)){
                    var fragBundle: Bundle = Bundle()
                    fragBundle.putString(Constants.Keys.TRANSFER_ID, transferId)
                    return FragmentTransactionSuccessOvoUser.newInstance(fragBundle)
                }
            }
        }
        return Fragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        updateTitle(Constants.Headers.TRANSFER_SUCCESS)
    }
}