package com.tokopedia.gamification.cracktoken;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.gamification.cracktoken.presentation.fragment.CrackTokenFragment;
import com.tokopedia.gamification.floatingtoken.model.TokenAsset;
import com.tokopedia.gamification.floatingtoken.model.TokenData;
import com.tokopedia.gamification.floatingtoken.model.TokenHome;
import com.tokopedia.gamification.floatingtoken.model.TokenUser;

import java.util.ArrayList;
import java.util.List;

public class CrackTokenActivity extends BaseSimpleActivity {

    public static final String EXTRA_TOKEN_DATA = "extra_token_data";

    private TokenData tokenData;

    public static Intent newInstance(Context context) {
        return new Intent(context, CrackTokenActivity.class);
    }

    public static Intent getIntent (Context context, TokenData tokenData){
        Intent intent = new Intent(context, CrackTokenActivity.class);
        intent.putExtra(EXTRA_TOKEN_DATA, tokenData);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        if (intent!= null && intent.hasExtra(EXTRA_TOKEN_DATA)) {
            tokenData = intent.getParcelableExtra(EXTRA_TOKEN_DATA);
        } else {
            List<String> imageUrls = new ArrayList<>();
            imageUrls.add("https://ecs7.tokopedia.net/assets/images/promo/tokopoints/egg-icon/032018-special/special-egg.png");
            imageUrls.add("https://ecs7.tokopedia.net/assets/images/promo/tokopoints/egg-icon/032018-special/special-egg-crack-4.png");
            imageUrls.add("https://ecs7.tokopedia.net/assets/images/promo/tokopoints/egg-icon/032018-special/special-egg-right.png");
            imageUrls.add("https://ecs7.tokopedia.net/assets/images/promo/tokopoints/egg-icon/032018-special/special-egg-left.png");

            TokenAsset tokenAsset = new TokenAsset();
            tokenAsset.setImageUrls(imageUrls);
            tokenAsset.setSmallImgUrl("https://ecs7.tokopedia.net/assets/images/promo/tokopoints/egg-icon/032018-float/plat1.png");

            TokenUser tokenUser = new TokenUser();
            tokenUser.setTokenUserID(1);
            tokenUser.setBackgroundImgUrl("https://ecs7.tokopedia.net/assets/images/gamification/background/bg-special-egg--tiny.png");
            tokenUser.setTokenAsset(tokenAsset);
            tokenUser.setTimeRemainingSeconds(3000);

            tokenData = new TokenData();
            TokenHome tokenHome= new TokenHome();
            tokenHome.setTokensUser(tokenUser);
            tokenData.setHome(tokenHome);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    protected Fragment getNewFragment() {
        return CrackTokenFragment.newInstance(tokenData);
    }
}
