import React, { Component } from 'react'
import { Provider } from 'react-redux'
import store from '../store/Store'
import PaymentInvoice from '../components/pages/invoice/PaymentInvoice'
import InvoiceErrorPage from '../components/pages/payment/ErrorPage'



class Root extends Component {
    render() {
        return (
            <Provider store={store}>
                {this.props.data.isError ? (
                    <InvoiceErrorPage screenProps={this.props} />
                    ) : (<PaymentInvoice screenProps={this.props} />)
                }
            </Provider>
        )
    }
}
export default Root

