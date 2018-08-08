
package com.tokopedia.challenges.view.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Me {

    @SerializedName("Id")
    @Expose
    private String id;
    @SerializedName("SubmissionCounts")
    @Expose
    private SubmissionCounts submissionCounts;
    @SerializedName("Liked")
    @Expose
    private boolean liked;
    @SerializedName("AuthProvider")
    @Expose
    private AuthProvider authProvider;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public SubmissionCounts getSubmissionCounts() {
        return submissionCounts;
    }

    public void setSubmissionCounts(SubmissionCounts submissionCounts) {
        this.submissionCounts = submissionCounts;
    }

    public AuthProvider getAuthProvider() {
        return authProvider;
    }

    public void setAuthProvider(AuthProvider authProvider) {
        this.authProvider = authProvider;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }
}
