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

//        GratificationPresenter(application)
//                .showGratificationInApp(weakActivity = WeakReference(this),
//                        gratificationId = "1",-up
//                        notificationEntryType = NotificationEntryType.ORGANIC, GratificationPresenter.GratifPopupCallback{})
    }
}