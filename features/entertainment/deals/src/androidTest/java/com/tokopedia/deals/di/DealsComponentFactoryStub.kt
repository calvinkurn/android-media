package com.tokopedia.deals.di

import android.app.Application
import android.content.Context

class DealsComponentFactoryStub : DealsComponentFactory() {

    override fun getDealsComponent(application: Application, context: Context): DealsComponent {
        val base = DaggerFakeBaseAppComponent.builder().fakeAppModule(FakeAppModule(context)).build()
        return DaggerDealsComponent.builder().dealsModule(DealsModule(context))
            .baseAppComponent(base).build()
    }
}
