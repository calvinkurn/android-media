import React, { Component } from 'react'
import { Provider } from 'react-redux'
import { StackNavigator } from 'react-navigation'
import store from '../store/Store'
import PaymentProcessing from '../components/pages/payment/PaymentProcessing'
import PaymentInvoice from '../components/pages/invoice/PaymentInvoice'




const App = StackNavigator({
    PaymentProcessing: {
        screen: PaymentProcessing,
        path: 'PaymentProcessing',
    }
}, { headerMode: 'none' })


class Root extends Component {
    render() {
        return (
            <Provider store={store}>
                <App screenProps={this.props} />
            </Provider>
        )
    }
}
export default Root

