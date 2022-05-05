package com.tokopedia.tokomember_seller_dashboard.view.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.tokomember_seller_dashboard.R
import com.tokopedia.tokomember_seller_dashboard.view.fragment.TokomemberCreateCardFragment

class TokomemberDashCreateActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment {
        return TokomemberCreateCardFragment.newInstance(intent.extras ?: Bundle())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toolbar.hide()
    }

    override fun onBackPressed() {
        when {
            supportFragmentManager.backStackEntryCount > 0 -> {
                supportFragmentManager.popBackStack()
            }
            supportFragmentManager.backStackEntryCount == 0 -> {
                val dialogCancel =
                    DialogUnify(this, DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE)
                dialogCancel.apply {
                    setTitle("Yakin batalkan program?")
                    setDescription("Pengaturan yang dibuat akan hilang kalau kamu batalkan proses pembuatan TokoMember, lho.")
                    setPrimaryCTAText("Lanjut")
                    setSecondaryCTAText("Batalkan Program")
                    setPrimaryCTAClickListener {
                        dismiss()
                    }
                    setSecondaryCTAClickListener {
                        super.onBackPressed()
                    }
                }
                dialogCancel.show()
            }
            else -> {
                return super.onBackPressed()
            }
        }
    }

    fun addFragment(fragment: Fragment, tag: String) {
        supportFragmentManager.beginTransaction()
            .add(com.tokopedia.abstraction.R.id.parent_view, fragment, tag)
            .addToBackStack(tag).commit()
    }

}