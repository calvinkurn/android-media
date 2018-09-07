package com.tokopedia.design.intdef;

public enum CurrencyEnum {
    RP ("Rp%s", false),
    USD ("$%s", true);

    private String format;
    private boolean useCommaAsThousand;
    private CurrencyEnum(String format, boolean useCommaAsThousand) {
        this.format = format;
        this.useCommaAsThousand = useCommaAsThousand;
    }

    public String getFormat() {
        return format;
    }

    public boolean isUseCommaAsThousand() {
        return useCommaAsThousand;
    }
}
