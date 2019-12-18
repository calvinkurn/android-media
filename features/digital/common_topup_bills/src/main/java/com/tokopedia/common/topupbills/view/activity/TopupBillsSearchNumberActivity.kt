package com.tokopedia.common.topupbills.view.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.common.topupbills.R
import com.tokopedia.common.topupbills.data.TopupBillsFavNumberItem
import com.tokopedia.common.topupbills.view.fragment.TopupBillsSearchNumberFragment
import java.util.*

/**
 * @author rizkyfadillah on 10/4/2017.
 */

open class TopupBillsSearchNumberActivity : BaseSimpleActivity(), TopupBillsSearchNumberFragment.OnClientNumberClickListener {

    protected lateinit var categoryId: String
    protected lateinit var clientNumberType: String
    protected lateinit var number: String
    protected lateinit var numberList: List<TopupBillsFavNumberItem>

    override fun getLayoutRes(): Int {
        return R.layout.activity_digital_search_number
    }

    override fun getParentViewResourceID(): Int {
        return R.id.parent_view
    }

    override fun getToolbarResourceID(): Int {
        return R.id.toolbar
    }

    override fun getScreenName(): String? {
        return TopupBillsSearchNumberActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val extras = intent.extras
        extras?.let {
            this.clientNumberType = extras.getString(EXTRA_CLIENT_NUMBER)
            this.number = extras.getString(EXTRA_NUMBER)
            this.numberList = extras.getParcelableArrayList(EXTRA_NUMBER_LIST)
        }
        super.onCreate(savedInstanceState)
        updateTitle(getString(R.string.digital_title_fav_number))
    }

    override fun getNewFragment(): androidx.fragment.app.Fragment {
        return TopupBillsSearchNumberFragment
                .newInstance(clientNumberType, number, numberList)
    }

    override fun onClientNumberClicked(orderClientNumber: TopupBillsFavNumberItem,
                                       inputNumberActionType: TopupBillsSearchNumberFragment.InputNumberActionType) {
        val intent = Intent()
        intent.putExtra(EXTRA_CALLBACK_CLIENT_NUMBER, orderClientNumber)
        intent.putExtra(EXTRA_CALLBACK_INPUT_NUMBER_ACTION_TYPE, inputNumberActionType.ordinal)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    companion object {

        const val EXTRA_NUMBER_LIST = "EXTRA_NUMBER_LIST"
        const val EXTRA_CLIENT_NUMBER = "EXTRA_CLIENT_NUMBER"
        const val EXTRA_NUMBER = "EXTRA_NUMBER"
        const val EXTRA_CATEGORY_ID = "EXTRA_CATEGORY_ID"

        const val EXTRA_CALLBACK_CLIENT_NUMBER = "EXTRA_CALLBACK_CLIENT_NUMBER"
        const val EXTRA_CALLBACK_INPUT_NUMBER_ACTION_TYPE = "EXTRA_CALLBACK_INPUT_NUMBER_ACTION_TYPE"

        fun newInstance(activity: Activity, clientNumberType: String,
                        number: String, numberList: List<TopupBillsFavNumberItem>): Intent {
            val intent = Intent(activity, TopupBillsSearchNumberActivity::class.java)
            intent.putExtra(EXTRA_CLIENT_NUMBER, clientNumberType)
            intent.putExtra(EXTRA_NUMBER, number)
            intent.putParcelableArrayListExtra(EXTRA_NUMBER_LIST, numberList as ArrayList<out Parcelable>)
            return intent
        }
    }

}
