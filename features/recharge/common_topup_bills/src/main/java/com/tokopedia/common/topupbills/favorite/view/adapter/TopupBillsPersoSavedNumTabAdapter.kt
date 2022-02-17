package com.tokopedia.common.topupbills.favorite.view.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.tokopedia.common.topupbills.data.prefix_select.TelcoCatalogPrefixSelect
import com.tokopedia.common.topupbills.favorite.view.fragment.TopupBillsPersoContactListFragment
import com.tokopedia.common.topupbills.favorite.view.fragment.TopupBillsPersoFavoriteNumberFragment
import java.util.ArrayList

class TopupBillsPersoSavedNumTabAdapter(
    fragment: Fragment,
    private var clientNumberType: String,
    private var number: String,
    private var dgCategoryIds: ArrayList<String>,
    private var currentCategoryName: String = "",
    private var loyaltyStatus: String = ""
): FragmentStateAdapter(fragment) {
    private var instance: HashMap<String, Fragment> = hashMapOf()

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            POSITION_CONTACT_LIST -> {
                if (!instance.containsKey(position.toString())) {
                    instance[position.toString()] = TopupBillsPersoContactListFragment.newInstance()
                }
                instance[position.toString()]!!
            }
            POSITION_FAVORITE_NUMBER -> {
                if (!instance.containsKey(position.toString())) {
                    instance[position.toString()] = TopupBillsPersoFavoriteNumberFragment.newInstance(
                        clientNumberType,
                        number,
                        currentCategoryName,
                        dgCategoryIds,
                        loyaltyStatus
                    )
                }
                instance[position.toString()]!!
            }
            else -> TopupBillsPersoContactListFragment.newInstance()
        }
    }

    override fun getItemCount(): Int {
        return SAVED_NUMBER_TAB_SIZE
    }

    companion object {
        const val SAVED_NUMBER_TAB_SIZE = 2
        const val POSITION_CONTACT_LIST = 0
        const val POSITION_FAVORITE_NUMBER = 1
    }
}