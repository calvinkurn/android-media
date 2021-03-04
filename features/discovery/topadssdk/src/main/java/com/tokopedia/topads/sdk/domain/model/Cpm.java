package com.tokopedia.topads.sdk.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Cpm implements Parcelable {

    private static final String KEY_TEMPLATE_ID = "template_id";
    private static final String KEY_NAME = "name";
    private static final String KEY_CPM_IMAGE = "image";
    private static final String KEY_BADGES = "badges";
    private static final String KEY_PROMOTED_TEXT = "promoted_text";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_URI = "uri";
    private static final String KEY_SHOP = "shop";
    private static final String KEY_CTA_TEXT = "button_text";
    private static final String KEY_LAYOUT = "layout";
    private static final String KEY_POSITION = "position";

    @SerializedName(KEY_TEMPLATE_ID)
    private int templateId;
    @SerializedName(KEY_NAME)
    private String name = "";
    @SerializedName(KEY_CPM_IMAGE)
    private CpmImage cpmImage;
    @SerializedName(KEY_BADGES)
    private List<Badge> badges = new ArrayList<>();
    @SerializedName(KEY_PROMOTED_TEXT)
    private String promotedText = "";
    @SerializedName(KEY_URI)
    private String uri = "";
    @SerializedName(KEY_DESCRIPTION)
    private String decription = "";
    @SerializedName(KEY_SHOP)
    private CpmShop cpmShop;
    @SerializedName(KEY_CTA_TEXT)
    private String cta = "";
    @SerializedName(KEY_LAYOUT)
    private int layout = 0;
    @SerializedName(KEY_POSITION)
    private int position = 0;

    public Cpm() {
    }

    public Cpm(JSONObject object) throws JSONException {
        if(!object.isNull(KEY_TEMPLATE_ID)){
            setTemplateId(object.getInt(KEY_TEMPLATE_ID));
        }
        if(!object.isNull(KEY_NAME)){
            setName(object.getString(KEY_NAME));
        }
        if(!object.isNull(KEY_CPM_IMAGE)){
            setCpmImage(new CpmImage(object.getJSONObject(KEY_CPM_IMAGE)));
        }
        if(!object.isNull(KEY_BADGES)) {
            JSONArray badgeArray = object.getJSONArray(KEY_BADGES);
            for (int i = 0; i < badgeArray.length(); i++) {
                badges.add(new Badge(badgeArray.getJSONObject(i)));
            }
        }
        if(!object.isNull(KEY_PROMOTED_TEXT)){
            setPromotedText(object.getString(KEY_PROMOTED_TEXT));
        }
        if(!object.isNull(KEY_URI)){
            setUri(object.getString(KEY_URI));
        }
        if(!object.isNull(KEY_DESCRIPTION)){
            setDecription(object.getString(KEY_DESCRIPTION));
        }
        if(!object.isNull(KEY_SHOP)){
            setCpmShop(new CpmShop(object.getJSONObject(KEY_SHOP)));
        }
        if(!object.isNull(KEY_CTA_TEXT)) {
            setCta(object.getString(KEY_CTA_TEXT));
        }
        if(!object.isNull(KEY_LAYOUT)) {
            setLayout(object.getInt(KEY_LAYOUT));
        }
        if(!object.isNull(KEY_POSITION)) {
            setPosition(object.getInt(KEY_POSITION));
        }
    }

    protected Cpm(Parcel in) {
        templateId = in.readInt();
        name = in.readString();
        cpmImage = in.readParcelable(CpmImage.class.getClassLoader());
        badges = in.createTypedArrayList(Badge.CREATOR);
        promotedText = in.readString();
        uri = in.readString();
        decription = in.readString();
        cpmShop = in.readParcelable(CpmShop.class.getClassLoader());
        cta = in.readString();
        layout = in.readInt();
        position = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(templateId);
        dest.writeString(name);
        dest.writeParcelable(cpmImage, flags);
        dest.writeTypedList(badges);
        dest.writeString(promotedText);
        dest.writeString(uri);
        dest.writeString(decription);
        dest.writeParcelable(cpmShop, flags);
        dest.writeString(cta);
        dest.writeInt(layout);
        dest.writeInt(position);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Cpm> CREATOR = new Creator<Cpm>() {
        @Override
        public Cpm createFromParcel(Parcel in) {
            return new Cpm(in);
        }

        @Override
        public Cpm[] newArray(int size) {
            return new Cpm[size];
        }
    };

    public String getCta() {
        return cta;
    }

    public void setCta(String cta) {
        this.cta = cta;
    }

    public String getDecription() {
        return decription;
    }

    public void setDecription(String decription) {
        this.decription = decription;
    }

    public CpmShop getCpmShop() {
        return cpmShop;
    }

    public void setCpmShop(CpmShop cpmShop) {
        this.cpmShop = cpmShop;
    }

    public int getTemplateId() {
        return templateId;
    }

    public void setTemplateId(int templateId) {
        this.templateId = templateId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CpmImage getCpmImage() {
        return cpmImage;
    }

    public void setCpmImage(CpmImage cpmImage) {
        this.cpmImage = cpmImage;
    }

    public List<Badge> getBadges() {
        return badges;
    }

    public void setBadges(List<Badge> badges) {
        this.badges = badges;
    }

    public String getPromotedText() {
        return promotedText;
    }

    public void setPromotedText(String promotedText) {
        this.promotedText = promotedText;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void setLayout(int layout) {
        this.layout = layout;
    }

    public int getLayout() {
        return this.layout;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return this.position;
    }
}
