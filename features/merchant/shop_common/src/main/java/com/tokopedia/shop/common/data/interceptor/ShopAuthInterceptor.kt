package com.tokopedia.shop.common.data.interceptor

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class ShopAuthInterceptor @Inject constructor(@ApplicationContext context: Context?,
                                              networkRouter: NetworkRouter?,
                                              userSessionInterface: UserSessionInterface?) : TkpdAuthInterceptor(context, networkRouter, userSessionInterface)