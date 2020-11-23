package com.tokopedia.promotionstarget

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tokopedia.promotionstarget.data.notification.NotificationEntryType
import com.tokopedia.promotionstarget.domain.presenter.GratificationPresenter
import java.lang.ref.WeakReference

class DummyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.promo_dummy_activoty)

        GratificationPresenter(application).showGratificationInApp(weakActivity = WeakReference(this),
                paymentID = 1L,
                gratifPopupCallback = object : GratificationPresenter.AbstractGratifPopupCallback() {},
                notificationEntryType = NotificationEntryType.ORGANIC,
                screenName = javaClass.name,
                timeout = 7000L, closeCurrentActivity = true)
    }
}