package com.tokopedia.common.topupbills.view.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.tokopedia.common.topupbills.data.prefix_select.TelcoCatalogPrefixSelect
import com.tokopedia.common.topupbills.view.fragment.TopupBillsContactListFragment
import com.tokopedia.common.topupbills.view.fragment.TopupBillsPersoFavoriteNumberFragment
import java.util.ArrayList

class TopupBillsPersoSavedNumTabAdapter(
    fragment: Fragment,
    private var clientNumberType: String,
    private var number: String,
    private var dgCategoryIds: ArrayList<String>,
    private var currentCategoryName: String = "",
    private var operatorData: TelcoCatalogPrefixSelect? = null
): FragmentStateAdapter(fragment) {
    private var instance: HashMap<String, Fragment> = hashMapOf()

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            POSITION_CONTACT_LIST -> {
                if (!instance.containsKey(position.toString())) {
                    instance[position.toString()] = TopupBillsContactListFragment.newInstance()
                }
                instance[position.toString()]!!
            }
            POSITION_FAVORITE_NUMBER -> {
                if (!instance.containsKey(position.toString())) {
                    instance[position.toString()] = TopupBillsPersoFavoriteNumberFragment.newInstance(
                        clientNumberType,
                        number,
                        operatorData,
                        currentCategoryName,
                        dgCategoryIds,
                        IS_MULTI_TAB
                    )
                }
                instance[position.toString()]!!
            }
            else -> TopupBillsContactListFragment.newInstance()
        }
    }

    override fun getItemCount(): Int {
        return SAVED_NUMBER_TAB_SIZE
    }

    companion object {
        const val SAVED_NUMBER_TAB_SIZE = 2
        const val POSITION_CONTACT_LIST = 0
        const val POSITION_FAVORITE_NUMBER = 1
        const val IS_MULTI_TAB = true
    }
}