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