package com.tokopedia.gamification.cracktoken.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.gamification.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hendry on 12/04/18.
 */

public class GeneralErrorCrackResult extends CrackResult {

    public GeneralErrorCrackResult(Context context) {

        setBenefitLabel(context.getString(R.string.error_reward_title));

        List<CrackBenefit> crackBenefits = new ArrayList<>();
        CrackBenefit crackBenefit = new CrackBenefit();
        crackBenefit.setText(context.getString(R.string.error_reward_message));
        crackBenefit.setColor(context.getString(R.string.expired_reward_color));
        crackBenefit.setSize(context.getString(R.string.expired_reward_size));
        crackBenefits.add(crackBenefit);

        Bitmap errorBitmap = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.gf_ic_toped_sorry);
        setBenefits(crackBenefits);
        setImageBitmap(errorBitmap);

        CrackButton returnButton = new CrackButton();
        returnButton.setTitle(context.getString(R.string.try_again_btn));
        returnButton.setType(CrackResult.TYPE_BTN_DISMISS);
        setReturnButton(returnButton);

        CrackButton ctaButton = new CrackButton();
        ctaButton.setTitle(context.getString(R.string.return_to_home));
        ctaButton.setApplink(ApplinkConst.HOME);
        ctaButton.setType(CrackResult.TYPE_BTN_REDIRECT);
        setCtaButton(ctaButton);

        CrackResultStatus crackResultStatus = new CrackResultStatus();
        crackResultStatus.setCode(CrackResult.STATUS_CODE_SERVER_ERROR);
        setResultStatus(crackResultStatus);
    }
}
