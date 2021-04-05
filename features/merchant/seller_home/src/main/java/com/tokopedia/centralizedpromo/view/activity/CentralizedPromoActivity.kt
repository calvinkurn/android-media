package com.tokopedia.centralizedpromo.view.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.applink.sellermigration.SellerMigrationApplinkConst
import com.tokopedia.centralizedpromo.view.fragment.CentralizedPromoFragment
import com.tokopedia.centralizedpromo.view.fragment.FirstVoucherBottomSheetFragment

class CentralizedPromoActivity : BaseSimpleActivity() {

    companion object {
        @JvmStatic
        fun createIntent(context: Context) = Intent(context, CentralizedPromoActivity::class.java)

        private const val FIRST_VOUCHER_BOTTOMSHEET_TAG = "first_voucher_bottomsheet"

        private const val IS_MVC_FIRST_TIME = "is_mvc_first_time"
        private const val VOUCHER_CREATION = "voucher_creation"
    }

    private val bottomSheet by lazy {
        FirstVoucherBottomSheetFragment.createInstance(this).apply {
            setCloseClickListener {
                this.dismiss()
            }
        }
    }

    private val promoCreationPreference by lazy {
        getSharedPreferences(VOUCHER_CREATION, Context.MODE_PRIVATE)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    override fun getNewFragment(): Fragment = CentralizedPromoFragment.createInstance()

    private fun handleIntent(intent: Intent?) {
        intent?.run {
            getStringExtra(SellerMigrationApplinkConst.SELLER_MIGRATION_APPLINKS_EXTRA)?.let { sellerMigrationNextDestinatonApplink ->
                RouteManager.getIntent(this@CentralizedPromoActivity, sellerMigrationNextDestinatonApplink).run {
                    addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    startActivity(this)
                }
                removeExtra(SellerMigrationApplinkConst.SELLER_MIGRATION_APPLINKS_EXTRA)
                return@run
            }
            data?.toString()?.let { uri ->
                if (uri.startsWith(ApplinkConstInternalSellerapp.CENTRALIZED_PROMO_FIRST_VOUCHER)) {
                    if (promoCreationPreference.getBoolean(IS_MVC_FIRST_TIME, true)) {
                        showBottomSheet()
                    } else {
                        RouteManager.route(this@CentralizedPromoActivity, ApplinkConstInternalSellerapp.CREATE_VOUCHER)
                    }
                }
            }
        }
    }

    private fun showBottomSheet() {
        bottomSheet.run {
            show(supportFragmentManager, FIRST_VOUCHER_BOTTOMSHEET_TAG)
        }
    }
}