package com.tokopedia.gamification.taptap.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.gamification.R;
import com.tokopedia.gamification.data.entity.CrackBenefitEntity;
import com.tokopedia.gamification.data.entity.CrackButtonEntity;
import com.tokopedia.gamification.data.entity.CrackResultEntity;
import com.tokopedia.gamification.data.entity.ResultStatusEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hendry on 12/04/18.
 */

public class GeneralErrorCrackResult extends CrackResultEntity {

    public GeneralErrorCrackResult(Context context) {

        setBenefitLabel(context.getString(R.string.error_reward_title));

        List<CrackBenefitEntity> crackBenefits = new ArrayList<>();
        CrackBenefitEntity crackBenefit = new CrackBenefitEntity();
        crackBenefit.setText(context.getString(R.string.error_reward_message));
        crackBenefit.setColor(context.getString(R.string.expired_reward_color));
        crackBenefit.setSize(context.getString(R.string.expired_reward_size));
        crackBenefits.add(crackBenefit);

        Bitmap errorBitmap = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.gf_ic_toped_sorry);
        setBenefits(crackBenefits);
        setImageBitmap(errorBitmap);

        CrackButtonEntity returnButton = new CrackButtonEntity();
        returnButton.setTitle(context.getString(R.string.try_again_btn));
        returnButton.setType(CrackResultEntity.TYPE_BTN_DISMISS);
        setReturnButton(returnButton);

        CrackButtonEntity ctaButton = new CrackButtonEntity();
        ctaButton.setTitle(context.getString(R.string.return_to_home));
        ctaButton.setApplink(ApplinkConst.HOME);
        ctaButton.setType(CrackResultEntity.TYPE_BTN_REDIRECT);
        setCtaButton(ctaButton);

        ResultStatusEntity crackResultStatus = new ResultStatusEntity();
        crackResultStatus.setCode(CrackResultEntity.STATUS_CODE_SERVER_ERROR);
        setResultStatus(crackResultStatus);
    }
}
