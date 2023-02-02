package com.tokopedia.mvc.presentation.summary.helper

import android.content.Context
import androidx.annotation.StringRes
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.mvc.presentation.creation.step1.VoucherTypeActivity
import com.tokopedia.mvc.presentation.creation.step2.VoucherInformationActivity
import com.tokopedia.mvc.presentation.creation.step3.VoucherSettingActivity
import com.tokopedia.mvc.presentation.product.list.ProductListActivity
import javax.inject.Inject
import com.tokopedia.mvc.R

class SummaryPagePageNameMapper @Inject constructor (
    @ApplicationContext private val context: Context
) {
    private fun getString(@StringRes resId: Int) = try {
        context.getString(resId)
    } catch (e: Exception) {
        ""
    }

    fun mapPageName(javaName: String): String {
        return when (javaName) {
            VoucherInformationActivity::class.java.name -> getString(R.string.smvc_informasi_kupon_label)
            VoucherTypeActivity::class.java.name -> getString(R.string.smvc_jenis_kupon_label)
            ProductListActivity::class.java.name -> getString(R.string.smvc_daftar_produk_label)
            VoucherSettingActivity::class.java.name -> getString(R.string.smvc_pengaturan_kupon_label)
            else -> ""
        }
    }
}
