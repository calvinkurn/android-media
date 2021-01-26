package com.tokopedia.flight.cancellationdetail.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.flight.R
import com.tokopedia.flight.cancellationdetail.presentation.fragment.FlightOrderCancellationDetailFragment
import com.tokopedia.flight.common.view.BaseFlightActivity
import com.tokopedia.flight.orderdetail.di.DaggerFlightOrderDetailComponent
import com.tokopedia.flight.orderdetail.di.FlightOrderDetailComponent

class FlightOrderCancellationDetailActivity : BaseFlightActivity(), HasComponent<FlightOrderDetailComponent> {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupToolbar()
    }

    override fun getNewFragment(): Fragment? =
            FlightOrderCancellationDetailFragment.createInstance(
                    intent.getStringExtra(EXTRA_SAVED_INSTANCE_CACHE_MANAGER_ID) ?: ""
            )

    override fun getComponent(): FlightOrderDetailComponent =
            DaggerFlightOrderDetailComponent.builder()
                    .flightComponent(flightComponent)
                    .build()

    override fun onCreateOptionsMenu(menu: Menu?): Boolean = true

    private fun setupToolbar() {
        toolbar.contentInsetStartWithNavigation = 0
        toolbar.setSubtitleTextColor(ContextCompat.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_N200))
        val title = getString(R.string.flight_cancellation_list_title)
        val subtitle = String.format(
                getString(R.string.flight_cancellation_list_id),
                intent.getIntExtra(EXTRA_REFUND_ID, 0)
        )
        updateTitle(title, subtitle)
    }

    companion object {
        const val EXTRA_SAVED_INSTANCE_CACHE_MANAGER_ID = "SAVED_INSTANCE_CACHE_MANAGER_ID"
        const val EXTRA_SAVED_CANCELLATION_DETAIL = "EXTRA_SAVED_CANCELLATION_DETAIL"
        const val EXTRA_REFUND_ID = "EXTRA_REFUND_ID"

        fun getIntent(context: Context, savedInstanceCacheId: String, refundId: Int): Intent =
                Intent(context, FlightOrderCancellationDetailActivity::class.java)
                        .putExtra(EXTRA_SAVED_INSTANCE_CACHE_MANAGER_ID, savedInstanceCacheId)
                        .putExtra(EXTRA_REFUND_ID, refundId)
    }
}