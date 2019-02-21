package com.tokopedia.contactus.inboxticket2.view.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.contactus.inboxticket2.view.fragment.FragmentProvideRating;

public class ActivityProvideRating extends BaseSimpleActivity {
    public static final String CLICKED_EMOJI = "clicked_emoji";
    public static final String PARAM_TICKET_ID = "ticket_id";
    public static Intent getInstance(Context context, int clickEmoji,String id) {
        Intent i = new Intent(context,ActivityProvideRating.class);
        i.putExtra(CLICKED_EMOJI,clickEmoji);
        i.putExtra(PARAM_TICKET_ID,id);
        return i;
    }

    @Override
    protected Fragment getNewFragment() {
        return FragmentProvideRating.newInstance(getIntent().getExtras());
    }
}
