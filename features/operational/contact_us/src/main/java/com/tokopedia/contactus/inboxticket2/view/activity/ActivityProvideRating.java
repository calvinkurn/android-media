package com.tokopedia.contactus.inboxticket2.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.contactus.inboxticket2.view.fragment.FragmentProvideRating;

public class ActivityProvideRating extends BaseSimpleActivity {
    public static final String CLICKED_EMOJI = "clicked_emoji";
    public static final String PARAM_TICKET_ID = "ticket_id";
    public static final String PARAM_COMMENT_ID = "comment_id";
    public static Intent getInstance(Context context, int clickEmoji,String id,String commentId) {
        Intent i = new Intent(context,ActivityProvideRating.class);
        i.putExtra(CLICKED_EMOJI,clickEmoji);
        i.putExtra(PARAM_TICKET_ID,id);
        i.putExtra(PARAM_COMMENT_ID,commentId);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar.setVisibility(View.GONE);
    }

    @Override
    protected Fragment getNewFragment() {
        return FragmentProvideRating.newInstance(getIntent().getExtras());
    }
}
