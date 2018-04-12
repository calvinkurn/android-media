package com.tokopedia.gamification.cracktoken.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.tokopedia.gamification.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hendry on 12/04/18.
 */

public class ExpiredCrackResult extends CrackResult {

    public ExpiredCrackResult(Context context, CrackResultStatus crackResultStatus) {

        setBenefitLabel("Maaf, Anda kurang cepat");

        List<CrackBenefit> crackBenefits = new ArrayList<>();
        crackBenefits.add(new CrackBenefit("Tunggu Kesempatan Lainnya", "#ffffff", "medium"));

        Bitmap errorBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.image_error_crack_result_expired);

        CrackButton returnButton = new CrackButton();
        returnButton.setTitle("Ok");
        returnButton.setType("dismiss");

        setBenefits(crackBenefits);
        setImageBitmap(errorBitmap);
        setReturnButton(returnButton);

        setResultStatus(crackResultStatus);
    }
}
