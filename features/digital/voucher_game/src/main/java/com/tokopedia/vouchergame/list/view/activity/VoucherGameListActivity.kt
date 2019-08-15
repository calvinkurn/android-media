package com.tokopedia.vouchergame.list.view.activity

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.vouchergame.list.di.DaggerVoucherGameListComponent
import com.tokopedia.vouchergame.list.di.VoucherGameListComponent
import com.tokopedia.vouchergame.list.view.fragment.VoucherGameListFragment

/**
 * Created by resakemal on 12/08/19.
 */
class VoucherGameListActivity : BaseSimpleActivity(), HasComponent<VoucherGameListComponent> {

    override fun getNewFragment(): Fragment {
        val bundle = intent.extras
        val menuId = bundle?.getInt(VoucherGameListFragment.EXTRA_MENU_ID) ?: 0
        val platformId = bundle?.getInt(VoucherGameListFragment.EXTRA_PLATFORM_ID) ?: 0
        return VoucherGameListFragment.createInstance(menuId, platformId)
    }

    override fun getComponent(): VoucherGameListComponent {
        return DaggerVoucherGameListComponent.builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .build()
    }

    companion object {
        fun newInstance(context: Context, menuId: Int = 0, platformId: Int = 0): Intent {
            val intent = Intent(context, VoucherGameListActivity::class.java)
            intent.putExtra(VoucherGameListFragment.EXTRA_MENU_ID, menuId)
            intent.putExtra(VoucherGameListFragment.EXTRA_PLATFORM_ID, platformId)
            return intent
        }
    }
}