package com.tokopedia.topupbills.searchnumber.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Parcelable
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.common.topupbills.view.activity.TopupBillsSearchNumberActivity
import com.tokopedia.common.topupbills.view.model.search.TopupBillsSearchNumberDataModel
import com.tokopedia.topupbills.R
import com.tokopedia.topupbills.searchnumber.di.DigitalTelcoSearchComponent
import com.tokopedia.topupbills.searchnumber.di.DigitalTelcoSearchInstance
import java.util.*

/**
 * @author rizkyfadillah on 10/4/2017.
 */

class DigitalSearchNumberActivity : TopupBillsSearchNumberActivity(), HasComponent<DigitalTelcoSearchComponent> {

    override fun getScreenName(): String? {
        return DigitalSearchNumberActivity::class.java.simpleName
    }

    override fun getNewFragment(): androidx.fragment.app.Fragment {
        return DigitalSearchNumberFragment.newInstance(clientNumberType, number, numberList)
    }

    override fun getComponent(): DigitalTelcoSearchComponent {
        return DigitalTelcoSearchInstance.getComponent(application)
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_search_number_telco
    }

    override fun getToolbarResourceID(): Int {
        return R.id.telco_toolbar_search
    }

    override fun getParentViewResourceID(): Int {
        return R.id.parent_view
    }

    companion object {
        fun newInstance(context: Context, clientNumberType: String,
                        number: String, numberList: List<TopupBillsSearchNumberDataModel>): Intent {
            val intent = Intent(context, DigitalSearchNumberActivity::class.java)
            intent.putExtra(EXTRA_CLIENT_NUMBER_TYPE, clientNumberType)
            intent.putExtra(EXTRA_CLIENT_NUMBER, number)
            intent.putParcelableArrayListExtra(EXTRA_NUMBER_LIST, numberList as ArrayList<out Parcelable>)
            return intent
        }
    }

}
