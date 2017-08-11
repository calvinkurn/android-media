package com.tokopedia.posapp.domain.model.product;

/**
 * Created by okasurya on 8/9/17.
 */

public class TermsDomain {
    private InstallmentRuleDomain rule6Months;
    private InstallmentRuleDomain rule3Months;
    private InstallmentRuleDomain rule12Months;
    private InstallmentRuleDomain rule18Months;

    public InstallmentRuleDomain getRule6Months() {
        return rule6Months;
    }

    public void setRule6Months(InstallmentRuleDomain rule6Months) {
        this.rule6Months = rule6Months;
    }

    public InstallmentRuleDomain getRule3Months() {
        return rule3Months;
    }

    public void setRule3Months(InstallmentRuleDomain rule3Months) {
        this.rule3Months = rule3Months;
    }

    public InstallmentRuleDomain getRule12Months() {
        return rule12Months;
    }

    public void setRule12Months(InstallmentRuleDomain rule12Months) {
        this.rule12Months = rule12Months;
    }

    public InstallmentRuleDomain getRule18Months() {
        return rule18Months;
    }

    public void setRule18Months(InstallmentRuleDomain rule18Months) {
        this.rule18Months = rule18Months;
    }
}
