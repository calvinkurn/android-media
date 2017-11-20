import { SessionModule } from 'NativeModules'
import { 
    BASE_API_URL_PAYMENT,
    BASE_API_URL_SCROOGE,
    BASE_API_URL_PCIDSS, 
    BASE_API_URL,
  } from './api.js'

// ============== Global Function ============== //
export const getUserId = () => {
    return SessionModule.getUserId()
        .then(res => { return res })
        .catch(err => console.log(err))
}

export const getAddrId = () => {
    return SessionModule.getAddrId()
      .then(res => { return res })
      .catch(err => console.log(err))
}

export const getAddrName = () => {
    return SessionModule.getAddressName()
        .then(res => { return res })
        .catch(err => console.log(err))
}

export const getShopId = () => {
    return SessionModule.getShopId()
      .then(res => { return res })
      .catch(err => console.log(err))
}

export const getShopName = () => {
    return SessionModule.getShopName()
    .then(res => {return res})
    .catch(err => console.log(err))
}

export const getEnv = () => {
    return SessionModule.getEnv()
      .then(res => { return res })
      .catch(err => console.log(err))
}

export const getBaseAPI = (env) => {
    let data_api = {}
  
    if (env === 'live'){
      const data_api = {
        api_url_payment: `${BASE_API_URL_PAYMENT.PRODUCTION}`,
        api_url_scrooge: `${BASE_API_URL_SCROOGE.PRODUCTION}`,
        api_url_pcidss: `${BASE_API_URL_PCIDSS.PRODUCTION}`,
        base_api_url: `${BASE_API_URL.PRODUCTION}`
      }
      return data_api
  
    } else {
      const data_api = {
        api_url_payment: `${BASE_API_URL_PAYMENT.STAGING}`,
        api_url_scrooge: `${BASE_API_URL_SCROOGE.STAGING}`,
        api_url_pcidss: `${BASE_API_URL_PCIDSS.STAGING}`,
        base_api_url: `${BASE_API_URL.STAGING}`
      }
      return data_api
    }
  }
// ============== Global Function ============== //



export const getCardType = (number) => {
    if (!number)
        return "";
    // visa
    var re = new RegExp("^4");
    if (number.match(re) != null)
        return "VISA";

    // Mastercard 
    var re = new RegExp("^5");
    if (number.match(re) != null)
        return "MASTER";

    // JCB
    re = new RegExp("^35(2[89]|[3-8][0-9])");
    if (number.match(re) != null)
        return "JCB";

    return "";
}


export  const ccFormat = (value) => {
    if (value) {
        var v = value.replace(/\s+/g, '').replace(/[^0-9]/gi, '')
        var matches = v.match(/\d{4,16}/g);
        var match = matches && matches[0] || ''
        var parts = []
        for (i=0, len=match.length; i<len; i+=4) {
          parts.push(match.substring(i, i+4))
        }
        if (parts.length) {
          return parts.join(' ')
        } else {
          return value
        }
    }
} 

export  const emailValidation = (email) => {
    if (email) {
       const re = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
       return re.test(email);
    }

    return false;
} 