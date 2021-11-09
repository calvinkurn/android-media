package com.tokopedia.homecredit.domain

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import javax.inject.Inject

class HomeCreditUseCase @Inject constructor(
     @ApplicationContext val context: Context)