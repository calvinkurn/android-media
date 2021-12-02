package com.tokopedia.shop.score.uitest.stub.common.util

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.shop.score.common.ShopScorePrefManager

class ShopScorePrefManagerStub(@ApplicationContext context: Context) : ShopScorePrefManager(context)