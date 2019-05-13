package com.tokopedia.config.url

/**
 * @author okasurya on 4/9/19.
 */

internal val live = Url(
        TYPE = Env.LIVE,
        WS = "ws".tokopediaCom(),
        API = "api".tokopediaCom(),
        WEB = "www".tokopediaCom(),
        MOBILEWEB = "m".tokopediaCom(),
        ACE = "ace".tokopediaCom(),
        TOME = "tome".tokopediaCom(),
        TA = "ta".tokopediaCom(),
        MOJITO = "mojito".tokopediaCom(),
        HADES = "hades".tokopediaCom(),
        MERLIN = "merlin".tokopediaCom(),
        ACCOUNTS = "accounts".tokopediaCom(),
        INBOX = "inbox".tokopediaCom(),
        CHAT = "https://chat.tokopedia.com",
        WS_CHAT = "wss://chat.tokopedia.com",
        GROUPCHAT =  "https://groupchat.tokopedia.com",
        WS_GROUPCHAT = "wss://groupchat.tokopedia.com",
        JS = "js".tokopediaCom(),
        KERO = "kero".tokopediaCom(),
        JAHE = "jahe".tokopediaCom(),
        GOLDMERCHANT = "goldmerchant".tokopediaCom(),
        PULSA = "pulsa".tokopediaCom(),
        PULSA_API = "pulsa-api".tokopediaCom(),
        PAY_ID = "https://pay.tokopedia.id/",
        PAY = "https://pay.tokopedia.com",
        PAYMENT = "payment".tokopediaCom(),
        GQL = "gql".tokopediaCom(),
        GW = "gw".tokopediaCom(),
        GALADRIEL = "galadriel".tokopediaCom(),
        FS = "https://fs.tokopedia.net/",
        TOKOCASH = "https://www.tokocash.com/",
        OMSCART = "omscart".tokopediaCom(),
        BOOKING = "booking".tokopediaCom(),
        TIKET = "tiket".tokopediaCom(),
        IMT =  "imt".tokopediaCom(),
        LAKU6 = "https://www.laku6.com"
)
