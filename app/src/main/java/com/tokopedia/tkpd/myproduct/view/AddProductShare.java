package com.tokopedia.tkpd.myproduct.view;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sromku.simple.fb.Permission;
import com.sromku.simple.fb.listeners.OnNewPermissionsListener;
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.ConnectionDetector;
import com.tkpd.library.utils.TwitterHandler;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.R2;
import com.tokopedia.tkpd.myproduct.fragment.AddProductFragment;
import com.tokopedia.tkpd.myproduct.utils.DelegateOnClick;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

/**
 * Created by noiz354 on 5/13/16.
 */
public class AddProductShare {
    public boolean isFacebookAuth = false;
    public boolean isTwitterAuth = false;

    ConnectionDetector cd;
    DelegateOnClick delegateOnClick;

    @Bind(R2.id.add_product_facebook_but)
    RelativeLayout facebookShareBut;

    @Bind(R2.id.twitter_but)
    RelativeLayout twitterShareBut;

    @Bind(R2.id.add_product_facebook)
    ImageView facebookShare;

    @Bind(R2.id.add_product_twitter)
    ImageView addProductTwitter;

    @Bind(R2.id.facebook_checkbut)
    CheckBox facebookCheckBut;

    @Bind(R2.id.twitter_checkbut)
    CheckBox twitterCheckBut;

    @Bind(R2.id.add_product_facebook_text)
    TextView facebookTextView;

    @Bind(R2.id.berbagi_title)
    TextView berbagiTitleTextView;

    public AddProductShare(View view){
        ButterKnife.bind(this, view);
        setToShareButton(view);
    }

//    static int count =0;

    @OnCheckedChanged(R2.id.facebook_checkbut)
    public void facebookChecked(boolean checked){
        butFacebookToggle(checked);
        isFacebookAuth = checked;
    }

    @OnCheckedChanged(R2.id.twitter_checkbut)
    public void twitterChecked(boolean checked){
        butTwitterToggle(checked);
    }

    @OnClick(R2.id.add_product_facebook_but)
    public void authorizeFacebook(){
        if ( delegateOnClick != null && delegateOnClick instanceof AddProductFragment){
            isFacebookAuth = !isFacebookAuth;
            facebookCheckBut.setChecked(isFacebookAuth);
        }
    }

    @OnClick(R2.id.twitter_but)
    public void authorizeTwitter() {
        twitterCall();
    }

    private void twitterCall() {
        if (delegateOnClick != null && delegateOnClick instanceof AddProductFragment) {
            cd = new ConnectionDetector(((AddProductFragment) delegateOnClick).getActivity());
            if (cd.isConnectingToInternet()) {
                if (!isTwitterAuth) {
                    TwitterHandler th = ((AddProductFragment) delegateOnClick).th;
                    if (!th.isTwitterLoggedInAlready()) {
                        ((AddProductFragment) delegateOnClick).showDialog();
                    }
                    butTwitterToggle(true);
                    isTwitterAuth = true;
                    twitterCheckBut.setChecked(true);
                } else {
                    butTwitterToggle(false);
                    isTwitterAuth = false;
                    twitterCheckBut.setChecked(false);
                }
            } else {
                cd.showNoConnection();
            }
        }
    }

    public void butFacebookToggle(boolean on){
        if(on){
            facebookShare.setImageResource(R.drawable.facebook_square_blue);
        }else{
            facebookShare.setImageResource(R.drawable.facebook_square);
        }
    }

    public void butTwitterToggle(boolean on){
        if (on){
            addProductTwitter.setImageResource(R.drawable.twitter_square_blue);
        } else{
            addProductTwitter.setImageResource(R.drawable.twitter_square);
        }
    }
    public DelegateOnClick getDelegateOnClick() {
        return delegateOnClick;
    }

    public void setDelegateOnClick(DelegateOnClick delegateOnClick) {
        this.delegateOnClick = delegateOnClick;
    }

    public void setToShareButton(View view){
        facebookShare.setVisibility(View.GONE);
        twitterShareBut.setVisibility(View.GONE);
        berbagiTitleTextView.setVisibility(View.GONE);
        facebookTextView.setText(view.getResources().getString(R.string.title_share));
        facebookTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);


    }
}
