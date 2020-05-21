package com.tokopedia.withdraw.saldowithdrawal.presentation.adapter.layoutmanager

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager


class NonScrollableLinerLayoutManager(context: Context) : LinearLayoutManager(context) {
    override fun canScrollVertically(): Boolean {
        return false
    }
}