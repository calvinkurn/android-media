
package com.tokopedia.groupchat.chatroom.domain.pojo.poll;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.groupchat.chatroom.domain.pojo.BaseGroupChatPojo;

import java.util.List;

public class ActivePollPojo extends BaseGroupChatPojo{

    @SerializedName("poll_id")
    @Expose
    private int pollId;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("question")
    @Expose
    private String question;
    @SerializedName("options")
    @Expose
    private List<Option> options = null;
    @SerializedName("poll_type_id")
    @Expose
    private int pollTypeId;
    @SerializedName("poll_type")
    @Expose
    private String pollType;
    @SerializedName("option_type_id")
    @Expose
    private int optionTypeId;
    @SerializedName("option_type")
    @Expose
    private String optionType;
    @SerializedName("status_id")
    @Expose
    private int statusId;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("start_time")
    @Expose
    private long startTime;
    @SerializedName("end_time")
    @Expose
    private long endTime;
    @SerializedName("statistic")
    @Expose
    private Statistic statistic;
    @SerializedName("is_answered")
    @Expose
    private boolean isAnswered;
    @SerializedName("winner_url")
    @Expose
    private String winnerUrl;

    public int getPollId() {
        return pollId;
    }

    public void setPollId(int pollId) {
        this.pollId = pollId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }

    public int getPollTypeId() {
        return pollTypeId;
    }

    public void setPollTypeId(int pollTypeId) {
        this.pollTypeId = pollTypeId;
    }

    public String getPollType() {
        return pollType;
    }

    public void setPollType(String pollType) {
        this.pollType = pollType;
    }

    public int getOptionTypeId() {
        return optionTypeId;
    }

    public void setOptionTypeId(int optionTypeId) {
        this.optionTypeId = optionTypeId;
    }

    public String getOptionType() {
        return optionType;
    }

    public void setOptionType(String optionType) {
        this.optionType = optionType;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public Statistic getStatistic() {
        return statistic;
    }

    public void setStatistic(Statistic statistic) {
        this.statistic = statistic;
    }

    public boolean isIsAnswered() {
        return isAnswered;
    }

    public void setIsAnswered(boolean isAnswered) {
        this.isAnswered = isAnswered;
    }

    public String getWinnerUrl() {
        return winnerUrl;
    }

    public void setWinnerUrl(String winnerUrl) {
        this.winnerUrl = winnerUrl;
    }

}
