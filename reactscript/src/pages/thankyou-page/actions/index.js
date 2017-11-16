import { NetworkModule } from 'NativeModules'



export const FETCH_DATA_DIGITAL = 'FETCH_DATA_DIGITAL'
export const fetchDataDigital = (order_id) => {
    console.log(order_id)
    return {
        type: FETCH_DATA_DIGITAL,
        payload: getData(order_id)
    }
}


const getData = async (order_id) => {
    const dataDigital = await getDataDigital(order_id)
    return dataDigital
}

const getDataDigital = async (order_id) => {
    console.log(order_id)
    const url_digital = 'https://pulsa-api-staging.tokopedia.com/v1.4/track/thankyou'
    const payloads = {
        secret_key: 'Tok0p3di4123',
        order_id: order_id
    }

    return NetworkModule.getResponse(url_digital, 'POST', JSON.stringify(payloads), true)
        .then(res => {
            const jsonResponse = JSON.parse(res)
            console.log(jsonResponse)
            return jsonResponse
        })
        .catch(err => {
            console.log(err)
        })
}