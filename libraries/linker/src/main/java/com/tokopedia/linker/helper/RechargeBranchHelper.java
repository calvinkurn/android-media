package com.tokopedia.linker.helper;

import android.content.Context;

import com.tokopedia.linker.LinkerConstants;
import com.tokopedia.linker.model.LinkerData;
import com.tokopedia.linker.model.RechargeLinkerData;

import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.util.BranchEvent;
import io.branch.referral.util.ContentMetadata;
import io.branch.referral.util.CurrencyType;

public class RechargeBranchHelper {

    public static void sendDigitalHomepageLaunchEvent(Context context, LinkerData linkerData){
        new BranchEvent(LinkerConstants.EVENT_DIGITAL_SUBHOMEPAGE_LAUNCHED)
                .addCustomDataProperty(LinkerConstants.PRODUCT_TYPE, LinkerConstants.PRODUCT_TYPE_DIGITAL)
                .addCustomDataProperty(LinkerConstants.USER_ID, linkerData.getUserId())
                .addContentItems(
                        new BranchUniversalObject().setContentMetadata(
                                new ContentMetadata().addCustomMetadata(LinkerConstants.CURRENCY, CurrencyType.IDR.toString())
                        )
                )
                .logEvent(context);
    }

    public static void sendDigitalScreenLaunchEvent(Context context, RechargeLinkerData rechargeLinkerData){
        LinkerData linkerData = rechargeLinkerData.getLinkerData();
        new BranchEvent(LinkerConstants.EVENT_DIGITAL_CATEGORY_LAUNCHED)
                .addCustomDataProperty(LinkerConstants.PRODUCT_CATEGORY, linkerData.getProductCategory())
                .addCustomDataProperty(LinkerConstants.USER_ID, linkerData.getUserId())
                .addContentItems(
                        new BranchUniversalObject().setContentMetadata(
                                new ContentMetadata()
                                        .setSku(rechargeLinkerData.getCategoryId())
                                        .addCustomMetadata(LinkerConstants.CURRENCY, CurrencyType.IDR.toString())
                        )
                )
                .logEvent(context);
    }

}
