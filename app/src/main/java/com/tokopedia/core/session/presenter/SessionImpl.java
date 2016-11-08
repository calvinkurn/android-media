package com.tokopedia.core.session.presenter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by m.normansyah on 04/11/2015.
 */
public class SessionImpl implements Session {
    Context context;
    SessionView sessionView;

    int finishTo;
    int whichFragmentKey;

    public SessionImpl(Context context){
        this.context = context;
        sessionView = (SessionView)context;
    }

    @Override
    public void initDataInstance() {

    }

    @Override
    public int setWhichFragmentKeyInvalid(Intent intent) {
        throw new RuntimeException("setWhichFragmentKeyInvalid don't use this");
    }

    @Override
    public void finishTo() {
        switch (finishTo){
            case SessionView.MOVE_TO_CART_TYPE:
            case SessionView.HOME:
                sessionView.moveTo(finishTo);
                break;
            default:
                Log.e(TAG, messageTAG + " move to unknown place !!!");
                break;
        }
    }

    @Override
    public void fetchExtras(Intent intent) {
        if(intent!=null){
            // set which activity should be moved after login process done
            int extra = intent.getExtras().getInt(SessionView.MOVE_TO_CART_KEY, SessionView.INVALID_MOVE_TYPE);
            if(extra !=SessionView.INVALID_MOVE_TYPE){
                finishTo = extra;
            }
            // set which fragment should be created
            int fragmentToShow = intent.getExtras().getInt(WHICH_FRAGMENT_KEY, WHICH_FRAGMENT_KEY_INVALID);
            if(fragmentToShow!=WHICH_FRAGMENT_KEY_INVALID){
                whichFragmentKey = fragmentToShow;
            }
        }
    }

    @Override
    public void saveDataBeforeRotate(Bundle outstate) {
        outstate.putInt(WHICH_FRAGMENT_KEY, whichFragmentKey);
        outstate.putInt(SessionView.MOVE_TO_CART_KEY, finishTo);
    }

    @Override
    public void fetchDataAfterRotate(Bundle instate) {
        if(instate!=null) {
            whichFragmentKey = instate.getInt(WHICH_FRAGMENT_KEY);
            finishTo = instate.getInt(SessionView.MOVE_TO_CART_KEY);
        }
        Log.d(TAG,messageTAG+"onCreate whichFragment : "+whichFragmentKey+" move to : "+finishTo);
    }

    @Override
    public boolean isAfterRotate() {
        return whichFragmentKey!=0;
    }

    @Override
    public int getWhichFragment() {
        return whichFragmentKey;
    }

    @Override
    public void setWhichFragment(int whichFragmentKey) {
        this.whichFragmentKey = whichFragmentKey;
    }
}
