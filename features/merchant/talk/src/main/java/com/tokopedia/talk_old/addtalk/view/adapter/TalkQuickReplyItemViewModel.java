package com.tokopedia.talk_old.addtalk.view.adapter;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.abstraction.base.view.adapter.Visitable;

/**
 * @author by StevenFredian on 15/05/18.
 */

public class TalkQuickReplyItemViewModel implements Visitable<QuickReplyTypeFactory>, Parcelable {

    private String id;
    private String text;

    public TalkQuickReplyItemViewModel(String id, String text) {
        this.id = id;
        this.text = text;
    }

    protected TalkQuickReplyItemViewModel(Parcel in) {
        id = in.readString();
        text = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(text);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TalkQuickReplyItemViewModel> CREATOR = new Creator<TalkQuickReplyItemViewModel>() {
        @Override
        public TalkQuickReplyItemViewModel createFromParcel(Parcel in) {
            return new TalkQuickReplyItemViewModel(in);
        }

        @Override
        public TalkQuickReplyItemViewModel[] newArray(int size) {
            return new TalkQuickReplyItemViewModel[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public int type(QuickReplyTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
