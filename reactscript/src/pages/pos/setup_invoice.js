import React, { Component } from 'react'
import { Provider } from 'react-redux'
import store from './store/Store'
// import PaymentProcessing from './components/PaymentProcessing'
import PaymentInvoice from './components/pages/invoice/PaymentInvoice'
import InvoiceErrorPage from './components/pages/payment/ErrorPage'
// import { StackNavigator } from 'react-navigation'



// const App = StackNavigator({
//     PaymentProcessing: {
//         screen: PaymentProcessing,
//         path: 'PaymentProcessing',
//       },
//     PaymentInvoice: {screen: PaymentInvoice}
// }, { headerMode: 'none' })


class Root extends Component {
    componentDidMount(){
        console.log(this.props)
    }

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

