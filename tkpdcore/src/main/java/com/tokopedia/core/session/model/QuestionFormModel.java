package com.tokopedia.core.session.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by m.normansyah on 05/11/2015.
 */
@Parcel
public class QuestionFormModel {
    @SerializedName("question")
    int Question;
    @SerializedName("example")
    String Example;
    @SerializedName("title")
    String Title;
    int type;

    public static final int INVALID_TYPE = 0;
    public static final int OTP_No_HP_TYPE = 1;
    public static final int OTP_Email_TYPE = 2;
    public static final int ANSWER_NO_HP_TYPE = 3;
    public static final int ANSWER_NO_REKENING_TYPE = 4;

    public QuestionFormModel(){

    }

    public QuestionFormModel(int question, String example, String title) {
        Question = question;
        Example = example;
        Title = title;
    }

    public int getQuestion() {
        return Question;
    }

    public void setQuestion(int question) {
        Question = question;
    }

    public String getExample() {
        return Example;
    }

    public void setExample(String example) {
        Example = example;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
