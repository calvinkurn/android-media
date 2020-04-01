package com.tkpd.library.utils.legacy;

/**
 * User {@link com.tokopedia.design.utils.CurrencyFormatHelper} or {@link com.tokopedia.design.utils.CurrencyFormatUtil}
 * modified by m.normansyah & steven.f
 * changed "," to "." for rupiah
 */
@Deprecated
public final class CurrencyFormatHelper {

	public static final int OTHER = -1;

    public static int convertRupiahToInt(String rupiah) {
		try {
        	rupiah = rupiah.replace("Rp", "");
        	rupiah = rupiah.replace(".", "");
        	rupiah = rupiah.replace(" ", "");
			return Integer.parseInt(rupiah);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
    }
}
