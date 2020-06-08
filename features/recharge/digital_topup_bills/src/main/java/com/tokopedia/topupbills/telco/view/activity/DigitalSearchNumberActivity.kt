package com.tokopedia.topupbills.telco.view.activity

import android.app.Activity
import android.content.Intent
import android.os.Parcelable
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.common.topupbills.data.TopupBillsFavNumberItem
import com.tokopedia.common.topupbills.view.activity.TopupBillsSearchNumberActivity
import com.tokopedia.topupbills.telco.view.di.DigitalTopupComponent
import com.tokopedia.topupbills.telco.view.di.DigitalTopupInstance
import com.tokopedia.topupbills.telco.view.fragment.DigitalSearchNumberFragment
import java.util.*

/**
 * @author rizkyfadillah on 10/4/2017.
 */

class DigitalSearchNumberActivity : TopupBillsSearchNumberActivity(), HasComponent<DigitalTopupComponent> {

    override fun getScreenName(): String? {
        return DigitalSearchNumberActivity::class.java.simpleName
    }

    override fun getNewFragment(): androidx.fragment.app.Fragment {
        return DigitalSearchNumberFragment
                .newInstance(clientNumberType, number, numberList)
    }

    override fun getComponent(): DigitalTopupComponent {
        return DigitalTopupInstance.getComponent(application)
    }

    companion object {
        fun newInstance(activity: Activity, clientNumberType: String,
                        number: String, numberList: List<TopupBillsFavNumberItem>): Intent {
            val intent = Intent(activity, DigitalSearchNumberActivity::class.java)
            intent.putExtra(EXTRA_CLIENT_NUMBER, clientNumberType)
            intent.putExtra(EXTRA_NUMBER, number)
            intent.putParcelableArrayListExtra(EXTRA_NUMBER_LIST, numberList as ArrayList<out Parcelable>)
            return intent
        }
    }

}
