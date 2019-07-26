package com.tokopedia.topupbills.telco.view.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import com.tokopedia.topupbills.R
import com.tokopedia.topupbills.telco.data.TelcoFavNumber
import com.tokopedia.topupbills.telco.view.fragment.DigitalSearchNumberFragment
import java.util.*

/**
 * @author rizkyfadillah on 10/4/2017.
 */

class DigitalSearchNumberActivity : BaseTelcoActivity(), DigitalSearchNumberFragment.OnClientNumberClickListener {

    private lateinit var categoryId: String
    private lateinit var clientNumberType: String
    private lateinit var number: String
    private lateinit var numberList: List<TelcoFavNumber>

    override fun getScreenName(): String? {
        return DigitalSearchNumberActivity::class.java.simpleName
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

    override fun getNewFragment(): android.support.v4.app.Fragment {
        return DigitalSearchNumberFragment
                .newInstance(clientNumberType, number, numberList)
    }

    override fun onClientNumberClicked(orderClientNumber: TelcoFavNumber,
                                       inputNumberActionType: DigitalSearchNumberFragment.InputNumberActionType) {
        val intent = Intent()
        intent.putExtra(EXTRA_CALLBACK_CLIENT_NUMBER, orderClientNumber)
        intent.putExtra(EXTRA_CALLBACK_INPUT_NUMBER_ACTION_TYPE, inputNumberActionType.ordinal)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    companion object {

        private val EXTRA_NUMBER_LIST = "EXTRA_NUMBER_LIST"
        private val EXTRA_CLIENT_NUMBER = "EXTRA_CLIENT_NUMBER"
        private val EXTRA_NUMBER = "EXTRA_NUMBER"
        private val EXTRA_CATEGORY_ID = "EXTRA_CATEGORY_ID"

        val EXTRA_CALLBACK_CLIENT_NUMBER = "EXTRA_CALLBACK_CLIENT_NUMBER"
        val EXTRA_CALLBACK_INPUT_NUMBER_ACTION_TYPE = "EXTRA_CALLBACK_INPUT_NUMBER_ACTION_TYPE"

        fun newInstance(activity: Activity, clientNumberType: String,
                        number: String, numberList: List<TelcoFavNumber>): Intent {
            val intent = Intent(activity, DigitalSearchNumberActivity::class.java)
            intent.putExtra(EXTRA_CLIENT_NUMBER, clientNumberType)
            intent.putExtra(EXTRA_NUMBER, number)
            intent.putParcelableArrayListExtra(EXTRA_NUMBER_LIST, numberList as ArrayList<out Parcelable>)
            return intent
        }
    }

}
