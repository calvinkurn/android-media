package com.tokopedia.loginregister.login.di

import android.app.Application
import androidx.annotation.VisibleForTesting
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.loginregister.redefineregisteremail.di.DaggerRedefineRegisterEmailComponent
import com.tokopedia.loginregister.redefineregisteremail.di.RedefineRegisterEmailComponent
import com.tokopedia.loginregister.registerinitial.di.DaggerRegisterInitialComponent
import com.tokopedia.loginregister.registerinitial.di.RegisterInitialComponent
import com.tokopedia.loginregister.shopcreation.di.DaggerShopCreationComponent
import com.tokopedia.loginregister.shopcreation.di.ShopCreationComponent

open class ActivityComponentFactory {

    open fun createLoginComponent(application: Application): LoginComponent {
        return DaggerLoginComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .build()
    }

    open fun createRegisterComponent(application: Application): RegisterInitialComponent {
        return DaggerRegisterInitialComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .build()
    }

    open fun createRedefineRegisterEmailComponent(application: Application): RedefineRegisterEmailComponent {
        return DaggerRedefineRegisterEmailComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .build()
    }

    open fun createShopCreationComponent(application: Application): ShopCreationComponent {
        return DaggerShopCreationComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .build()
    }

    companion object {
        private var sInstance: ActivityComponentFactory? = null

        @VisibleForTesting
        var instance: ActivityComponentFactory
            get() {
                if (sInstance == null) sInstance = ActivityComponentFactory()
                return sInstance!!
            }
            set(instance) {
                sInstance = instance
            }
    }
}
