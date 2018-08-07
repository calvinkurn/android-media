
package com.tokopedia.challenges.domain.model;

import java.util.HashMap;
import java.util.Map;

public class Me {

    private String id;
    private SubmissionCounts submissionCounts;
    private AuthProvider authProvider;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

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

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
