package com.tokopedia.talk_old.addtalk.view.adapter;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.abstraction.base.view.adapter.Visitable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by StevenFredian on 15/05/18.
 */

public class TalkQuickReplyViewModel implements Visitable<QuickReplyTypeFactory>, Parcelable {
    public static final String TYPE = "quick_reply";
    
    private List<TalkQuickReplyItemViewModel> list;

    public List<TalkQuickReplyItemViewModel> getList() {
        return list;
    }

    public void setList(List<TalkQuickReplyItemViewModel> list) {
        this.list = list;
    }

    @Override
    public int type(QuickReplyTypeFactory typeFactory) {
        return 0;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.list);
    }

    public TalkQuickReplyViewModel() {
    }

    protected TalkQuickReplyViewModel(Parcel in) {
        this.list = new ArrayList<>();
        in.readList(this.list, Visitable.class.getClassLoader());
    }

    public static final Creator<TalkQuickReplyViewModel> CREATOR = new Creator<TalkQuickReplyViewModel>() {
        @Override
        public TalkQuickReplyViewModel createFromParcel(Parcel source) {
            return new TalkQuickReplyViewModel(source);
        }

        @Override
        public TalkQuickReplyViewModel[] newArray(int size) {
            return new TalkQuickReplyViewModel[size];
        }
    };
}
