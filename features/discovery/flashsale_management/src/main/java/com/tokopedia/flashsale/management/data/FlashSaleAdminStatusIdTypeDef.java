package com.tokopedia.flashsale.management.data;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.tokopedia.flashsale.management.data.FlashSaleAdminStatusIdTypeDef.NAKAMA_ACCEPTED;
import static com.tokopedia.flashsale.management.data.FlashSaleAdminStatusIdTypeDef.NAKAMA_REJECTED;
import static com.tokopedia.flashsale.management.data.FlashSaleAdminStatusIdTypeDef.NAKAMA_TAKEOUT;
import static com.tokopedia.flashsale.management.data.FlashSaleAdminStatusIdTypeDef.NOT_REVIEWED;
import static com.tokopedia.flashsale.management.data.FlashSaleAdminStatusIdTypeDef.SYSTEM_TAKEOUT;

@Retention(RetentionPolicy.SOURCE)
@IntDef({NOT_REVIEWED, NAKAMA_ACCEPTED, NAKAMA_REJECTED, SYSTEM_TAKEOUT, NAKAMA_TAKEOUT})
public @interface FlashSaleAdminStatusIdTypeDef {
    int NOT_REVIEWED = 0;
    int NAKAMA_ACCEPTED = 1;
    int NAKAMA_REJECTED = 2;
    int SYSTEM_TAKEOUT = 3;
    int NAKAMA_TAKEOUT = 4;
}
